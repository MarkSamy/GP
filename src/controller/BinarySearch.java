public class BinarySearch {

	private long[] data;
	private int size;

	public BinarySearch(long[] data,int size)
	{
		this.data = data;
		this.size = size;
	}
	
	public long binarySearch(long key) {
		int low = 0;
		int high = size - 1;
		int middle = 0  ;
		while (high >= low) {
			middle = (low + high) / 2;
			if (data[middle] == key) {
				return data[middle];
			}
			if (data[middle] < key) {
				low = middle + 1;
			}
			if (data[middle] > key) {
				high = middle - 1;
			}
		}
		return data[middle];
	}
}