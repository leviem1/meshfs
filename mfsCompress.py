#Created by Aaron Duran, Mark Hedrick, and Levi Muniz
import bz2

class mfsCompress(object):
	"Compresses data on the fly"

	def compress(self, data):
		return bz2.compress(data)
				
	def decompress(self, data):
		return bz2.decompress(data)