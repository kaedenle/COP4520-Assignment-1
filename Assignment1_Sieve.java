import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Primes implements Runnable{
	private Trackers track;
	private int id;
	public static boolean flag;
	public static boolean setMultipleFlag;
	
	public Primes(Trackers t, int id) {
		track = t;
		this.id = id;
	}
	
	@Override
	public void run() {
		int cur = 2;
		
		int end = 0;
		int counter = 0;
		
		while(cur < track.getMax()) {
			if(track.array[cur - 1] == false) {
				cur++;
				continue;
			}
			int next = cur * cur;
			int delta = (int) Math.ceil((Math.ceil(track.getMax() + cur - next)/cur)/track.NUM_THREADS);
			counter = next + (id * delta * cur);
			end = next + ((id + 1) * delta * cur) - 1;
			
			if(end < counter) {
				return;
			}
			
			//System.out.println(counter + " " + end + " " + cur + " " + Thread.currentThread().getName());
			while(counter <= end && counter <= track.getMax()) {
				if(0 > counter) {
					return;
				}
				track.array[counter - 1] = false;

				//System.out.println(counter + " Current Multiplier: " + cur);
				counter += cur;
			}
			cur++;
			//return; //Safety
		}
	}
	
}
class Trackers{
	private final int MAX_VALUE;
	public boolean[] array;
	public int NUM_THREADS;
	
	public Trackers(int MAX_VALUE, int NUM_THREADS) {
		this.MAX_VALUE = MAX_VALUE;
		this.NUM_THREADS = NUM_THREADS;
		array = new boolean[MAX_VALUE];
		for(int i = 0; i < MAX_VALUE; i++) {
			array[i] = true;
		}
		//1 is not prime
		array[0] = false;
	}
	
	public int getMax() {
		return MAX_VALUE;
	}

}

public class Assignment1_Sieve {
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		final int THREADS = (args.length > 1) ? Integer.valueOf(args[0]) : 8;
		//(int)Math.pow(10,  8)
		Trackers t = new Trackers((int)Math.pow(10,  8), THREADS);
		Thread[] tList = new Thread[THREADS];
		
		for(int i = 0; i < THREADS; i++) {
			tList[i] = new Thread(new Primes(t, i));
		}
		for(Thread thread: tList)
			thread.start();
		
		//wait till threads stop to continue main one
		for(Thread thread: tList) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long endTime = System.nanoTime();
        double totalTime = (endTime - startTime);
        String timeOutput = (totalTime/1000000 + " ms");
        
        long sum = 0;
        int amount = 0;
        int[] top = new int[10];
        int counter = 0;
        String s = "";
        
        //get relevant data
        for(int i = t.array.length - 1; 0 <= i ; i--) {
        	if(t.array[i] == true) {
        		sum += (i + 1);
        		amount += 1;
        		if(counter < 10) {
        			top[counter] = i + 1;
        			counter++;
        		}
        	}
        }
        
        //construct string to be written
        s += (timeOutput + " " + amount + " " + sum + " ");
        for(int i = top.length - 1; i >= 0; i--) {
        	s += top[i] + " ";
        }
        
        //Write to file
        Path fileName = Path.of(Paths.get("")
                .toAbsolutePath()
                .toString() + "/primes.txt");
        // Writing into the file
        try {
			Files.writeString(fileName, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
