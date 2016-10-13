/**
 * Created by Aaron Duran on 10/13/16.
 */

import java.io.*;
import java.util.zip.*;
import java.nio.charset.*;

public class Compressor {

    byte[] dataToCompress;

    public Compressor(String info)
    {
        dataToCompress = info.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] compress()
    {
        try
        {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length);
            try
            {
                GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
                try
                {
                    zipStream.write(dataToCompress);
                }
                finally
                {
                    zipStream.close();
                }
            }
            finally
            {
                byteStream.close();
            }

            byte[] compressedData = byteStream.toByteArray();
            return compressedData;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}