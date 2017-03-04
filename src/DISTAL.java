import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The DISTAL class distributes a file across the linked computers with a variable number of whole
 * copies, stripes, and striped copies.
 *
 * @author Aaron Duran
 * @version 1.0.0
 */
class DISTAL {

  private static LinkedHashMap<String, Long> valueSorter(LinkedHashMap<String, Long> storageMap) {

    LinkedHashMap<String, Long> sortedMap = new LinkedHashMap();

    //put something in the map to compare against
    sortedMap.put("temp", -1L);

    for (String key : storageMap.keySet()) {
      Long storageAmount = storageMap.get(key);
      boolean isBroken = false;

      for (String sortedKey : sortedMap.keySet()) {
        if (storageAmount >= sortedMap.get(sortedKey)) {
          //reorder the map when a storage value is larger than one that is already in the map
          LinkedHashMap<String, Long> reorderStorageMap =
              (LinkedHashMap<String, Long>) sortedMap.clone();
          sortedMap.clear();
          for (String reorderKey : reorderStorageMap.keySet()) {
            if (reorderKey.equals(sortedKey)) {
              sortedMap.put(key, storageAmount);
            }
            sortedMap.put(reorderKey, reorderStorageMap.get(reorderKey));
          }
          isBroken = true;
          break;
        }
      }

      if (!isBroken) {
        sortedMap.put(key, storageMap.get(key));
      }
    }
    return sortedMap;
  }

  /**
   * This method is used determines which load balance which computers will receive a file and its
   * stripes. The method also sends the file to the appropriate computers and updates the catalog
   * file.
   *
   * @param uploadFilePath the file path of the file that is to be distributed
   * @param filePathInCatalog where the file is to be put in the catalog.
   */
  static void distributor(String uploadFilePath, String filePathInCatalog) {
    String userAccount;
    try {
      userAccount = filePathInCatalog.substring(0, filePathInCatalog.indexOf("/"));
    } catch (Exception e) {
      userAccount = filePathInCatalog;
    }

    //import properties
    int numOfStripes = Integer.parseInt(MeshFS.properties.getProperty("numStripes"));
    int numOfStripedCopies = Integer.parseInt(MeshFS.properties.getProperty("numStripeCopy"));
    int numOfWholeCopies = Integer.parseInt(MeshFS.properties.getProperty("numWholeCopy"));
    uploadFilePath = MeshFS.properties.getProperty("repository") + uploadFilePath;
    String manifestFileLocation = MeshFS.properties.getProperty("repository") + ".manifest.json";
    JSONObject manifestFile = JSONManipulator.getJSONObject(manifestFileLocation);
    String catalogFileLocation = MeshFS.properties.getProperty("repository") + ".catalog.json";
    int portNum = Integer.valueOf(MeshFS.properties.getProperty("portNumber"));

    //make the JTree show that the file is being distributed
    JSONManipulator.addToIndex(
        userAccount,
        uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1)
            + " (distributing)",
        catalogFileLocation,
        userAccount);

    try {
      String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1);
      long sizeOfFile = FileUtils.getSize(uploadFilePath);
      long sizeOfStripe;

      //create a map of the amount of available storage on each computer
      LinkedHashMap<String, Long> compStorageMap = JSONManipulator.createStorageMap(manifestFile);

      //sort the compStorageMap by descending available storage
      LinkedHashMap<String, Long> sortedCompStorageMap = valueSorter(compStorageMap);

      //don't use stripes if a file is less than 4096 byte
      if (sizeOfFile <= 4096L) {
        numOfWholeCopies += numOfStripedCopies;
        numOfStripes = 0;
        numOfStripedCopies = 0;
      }

      //create a list of computers that can store a whole copy.
      List<String> computersForWholes = new ArrayList<>();

      while (true) {

        for (String macAddress : sortedCompStorageMap.keySet()) {
          if (computersForWholes.size() == numOfWholeCopies) {
            break;
          }
          if ((!computersForWholes.contains(macAddress))
              && sortedCompStorageMap.get(macAddress) >= sizeOfFile) {
            computersForWholes.add(macAddress);
            sortedCompStorageMap.replace(
                macAddress, sortedCompStorageMap.get(macAddress) - sizeOfFile);
          }
        }
        sortedCompStorageMap = valueSorter(sortedCompStorageMap);

        int numOfComputersUsed = sortedCompStorageMap.size();

        //use stripes only when the number of computers available exceeds the number of requested redundancies
        if (numOfComputersUsed <= numOfStripedCopies + numOfWholeCopies) {
          numOfWholeCopies = numOfComputersUsed;
          numOfStripes = 0;
          numOfStripedCopies = 0;
        }

        //don't use stripes if there is only one stripe
        if (numOfStripes == 1) {
          numOfWholeCopies += numOfStripedCopies;
          numOfStripes = 0;
          numOfStripedCopies = 0;
        }

        //dynamic resigning of number of Wholes by number of computers that are on
        if (numOfComputersUsed < numOfWholeCopies) {
          numOfWholeCopies = numOfComputersUsed;
        }

        //dynamic resigning of number of Stripes by number of computers that are on
        if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
          numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
        }

        //define how big each stripe should be
        try {
          sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);
        } catch (Exception e) {
          sizeOfStripe = 0L;
        }

        //remove any computer that cannot store a stripe
        boolean finalComputerCount = true;
        if (sizeOfStripe != 0L) {
          for (String macAddress : sortedCompStorageMap.keySet()) {
            if (sortedCompStorageMap.get(macAddress) < sizeOfStripe) {
              sortedCompStorageMap.remove(macAddress);
              finalComputerCount = false;
            }
          }
        }

        //keep dynamically reassigning computers until all listed computers can hold the files that they will be given.
        if (finalComputerCount) {
          break;
        }
      }

      //define which computers get stripes
      List<String> computersForStripes = new ArrayList<>();
      for (String macAddress : sortedCompStorageMap.keySet()) {
        if (computersForStripes.size() == numOfStripedCopies * numOfStripes) {
          break;
        }
        computersForStripes.add(macAddress);
      }

      //create a unique filename for the uploaded file
      JSONObject jsonObj = JSONManipulator.getJSONObject(catalogFileLocation);
      String currentName = jsonObj.get("currentName").toString();
      String newName = incrementName(currentName);

      //create the list that will be used to distribute the wholes and stripes
      List<List<String>> stripes = new ArrayList<>();

      //first list is for the computers that will receive wholes
      stripes.add(computersForWholes);

      //if only computer is available to hold stripes, then do not use stripes.

      if (numOfStripes != 0) {
        //create a list for each stripe
        for (int copy = 0; copy < numOfStripes; copy++) {
          stripes.add(new ArrayList<>());
        }

        //balancing the number of computers that each stripe is sent to.
        for (int copy = 0; copy < numOfStripedCopies; copy++) {
          for (int currentStripe = 0; currentStripe < numOfStripes; currentStripe++) {
            stripes
                .get(currentStripe + 1)
                .add(computersForStripes.get((copy * numOfStripes) + currentStripe));
          }
        }
      }

      //send the files to the respective computers
      sendFiles(stripes, uploadFilePath, sizeOfFile, newName);

      //update the JSON file in order to update the JTree
      JSONManipulator.writeJSONObject(
          catalogFileLocation,
          JSONManipulator.removeItem(
              jsonObj,
              userAccount
                  + "/"
                  + uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1)
                  + " (distributing)"));
      JSONManipulator.addToIndex(
          stripes,
          filePathInCatalog,
          fileName,
          catalogFileLocation,
          newName,
          userAccount,
          sizeOfFile);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String incrementName(String name) {
    //creates a new unique filename
    String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    int incrementReverseIndex = 1;
    String toAdd = "";

    while (true) {
      try {
        //advance to the next character.
        char lastChar = name.charAt(name.length() - incrementReverseIndex);
        toAdd += alphabet.charAt(alphabet.indexOf(lastChar) + 1);
        break;
      } catch (IndexOutOfBoundsException iobe) {
        //if have reached the end of the alphabet, loop back to the beginning and use the next character
        toAdd += alphabet.charAt(0);
        incrementReverseIndex++;
      }
    }
    String newName = name.substring(0, name.length() - (toAdd.length()));
    for (int reverseIndex = (toAdd.length() - 1); reverseIndex >= 0; reverseIndex--) {
      newName += toAdd.charAt(reverseIndex);
    }
    return newName;
  }

  private static void sendFiles(
      List<List<String>> stripes, String sourceFileLocationOld, long fileSize, String outName) {
    List<Thread> parentThreads = new ArrayList<>();

    long sizeOfStripe;
    try {
      sizeOfStripe = ((fileSize / (stripes.size() - 1) + 1));
    } catch (Exception e) {
      sizeOfStripe = 0L;
    }
    //rename the original file to what the distributed whole file will be

    new File(sourceFileLocationOld)
        .renameTo(
            new File(
                sourceFileLocationOld.substring(
                        0, sourceFileLocationOld.lastIndexOf(File.separator) + 1)
                    + outName
                    + "_w"));
    final String sourceFileLocation =
        sourceFileLocationOld.substring(0, sourceFileLocationOld.lastIndexOf(File.separator) + 1)
            + outName
            + "_w";
    JSONObject manifestFile =
        JSONManipulator.getJSONObject(
            MeshFS.properties.getProperty("repository") + ".manifest.json");

    //create a new thread for each stripe, so all stripes are written simultaneously
    for (int stripe = -1; stripe < (stripes.size() - 1); stripe++) {
      parentThreads.add(
          new Thread(
              new sendFilesThreading(
                  sizeOfStripe,
                  fileSize,
                  sourceFileLocation,
                  manifestFile,
                  stripes,
                  stripe,
                  outName)));
    }
    for (Thread parent : parentThreads) {
      parent.start();
    }

    //wait for all the treads to close
    for (Thread parent : parentThreads) {
      if (parent.isAlive()) {
        try {
          parent.join();
        } catch (InterruptedException ignored) {
        }
      }
    }

    //delete the original file
    FileUtils.removeFile(sourceFileLocation);
  }
}

class sendFilesThreading implements Runnable {
  private final long sizeOfStripe;
  private final long fileSize;
  private final String sourceFileLocation;
  private final JSONObject manifestFile;
  private final List<List<String>> stripes;
  private final int stripe;
  private final String outName;

  sendFilesThreading(
      long sizeOfStripe,
      long fileSize,
      String sourceFileLocation,
      JSONObject manifestFile,
      List<List<String>> stripes,
      int stripe,
      String outName) {
    this.sizeOfStripe = sizeOfStripe;
    this.fileSize = fileSize;
    this.sourceFileLocation = sourceFileLocation;
    this.manifestFile = manifestFile;
    this.stripes = stripes;
    this.stripe = stripe;
    this.outName = outName;
  }

  private void writeSendStripe() throws IOException {
    List<Thread> childThreads = new ArrayList<>();

    if (stripe == -1) {
      //send the wholes
      for (String computerToReceive : stripes.get(stripe + 1)) {
        Thread child =
            new Thread(
                () -> {
                  try {
                    FileClient.sendFile(
                        (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                        Integer.valueOf(MeshFS.properties.getProperty("portNumber")),
                        MeshFS.properties.getProperty("repository")
                            + File.separator
                            + outName
                            + "_w",
                        MeshFS.properties.getProperty("remoteUUID"));
                    FileClient.receiveReport(
                        (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                        Integer.valueOf(MeshFS.properties.getProperty("portNumber")));
                  } catch (IOException ioe) {
                    ioe.printStackTrace();
                  }
                });

        childThreads.add(child);
      }
    } else if (stripe == stripes.size() - 2) {
      //send the last stripe, taking into account that he the last stripe to usually smaller than other stripes.
      FileUtils.writeStripe(
          sourceFileLocation,
          MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe,
          (sizeOfStripe * stripe),
          sizeOfStripe - ((sizeOfStripe * (stripes.size() - 1)) - fileSize));
      for (String computerToReceive : stripes.get(stripe + 1)) {
        Thread child =
            new Thread(
                () -> {
                  try {
                    FileClient.sendFile(
                        (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                        Integer.valueOf(MeshFS.properties.getProperty("portNumber")),
                        MeshFS.properties.getProperty("repository")
                            + File.separator
                            + outName
                            + "_s"
                            + stripe,
                        MeshFS.properties.getProperty("remoteUUID"));
                    FileClient.receiveReport(
                        (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                        Integer.valueOf(MeshFS.properties.getProperty("portNumber")));
                  } catch (IOException ioe) {
                    ioe.printStackTrace();
                  }
                });
        childThreads.add(child);
      }
    } else {
      //send all other stripes
      FileUtils.writeStripe(
          sourceFileLocation,
          MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe,
          (sizeOfStripe * stripe),
          sizeOfStripe);
      for (String computerToReceive : stripes.get(stripe + 1)) {
        Thread child =
            new Thread(
                () -> {
                  try {
                    FileClient.sendFile(
                        (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                        Integer.valueOf(MeshFS.properties.getProperty("portNumber")),
                        MeshFS.properties.getProperty("repository")
                            + File.separator
                            + outName
                            + "_s"
                            + stripe,
                        MeshFS.properties.getProperty("remoteUUID"));
                    FileClient.receiveReport(
                        (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                        Integer.valueOf(MeshFS.properties.getProperty("portNumber")));
                  } catch (IOException ioe) {
                    ioe.printStackTrace();
                  }
                });

        childThreads.add(child);
      }
    }

    //send all copies of stripe simultaneously
    for (Thread child : childThreads) {
      child.start();
    }

    //wait for all copies to finish sending before deleting the stripe on the master
    for (Thread child : childThreads) {
      if (child.isAlive()) {
        try {
          child.join();
        } catch (InterruptedException ignored) {
        }
      }
    }
    FileUtils.removeFile(
        MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe);
  }

  public void run() {
    try {
      writeSendStripe();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
