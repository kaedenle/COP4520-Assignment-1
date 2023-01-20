# COP4520-Assignment-1
## RUNNING
In the directory Assignment1_Sieve.java is in, run...
- javac Assignment1_Sieve.java
  - This will create 3 new classes in the directory
- java Assignment1_Sieve [THREAD_NUM, default=8] [MAX, default = 10<sup>8</sup>]
  - The last two args in [] are optional, however you either include both or none

This will produce a file named primes.txt

## OUTPUT
(execution time in ms) (number of primes found) (sum of all primes) (last ten primes)

## APPROACH
The Sieve of Eratosthenes was implemented.
There will be an array of MAX length, with MAX being the upper bound.
- The array will be initialized to true (except the first value, representing a value of 1 and index 0)
- For each index found to be true in the array, each number that's a multiple of that index + 1 will be set to false
- This is where the threads will do their work, in marking booleans to be false
- Each thread will evaluate a range of multiples to be false. This is repeated across all found primes where "array[index]" hadn't been set to false.

## CORRECTNESS
The program produces the correct amount of primes, sum of primes, and the last 10 primes of the range. This will be shown in Experiemental Evaluations.
The work is balanced. 

Each thread is dynamically given a range of numbers that is equal in length to the other threads' ranges. Due to each operation per number being constant time, there is no variation in time among the ranges. Thus the work is being balanced as long as the ranges are equal in length.

## EFFICIENCY
Each thread will take an equal amount of numbers per multiple M to consider.
Within the range each thread is considering, they will increment through each range by M.
- This jump of M within each range is why each range needs to begin with a multiple.
- Without this, each range could start with a non-multiple of M and increment M from there and potentially flipping a prime to false.

Once a thread is done with their range for multiple M they move onto the next multiple, irrespective of whether the other threads finished their range for multiple M.
- This is due to the fact that each range's results is independent of each other
- This independence allows each thread to do their own work without having to wait for another thread.
- This program's focus on threads doing independent work left no room for mutual exclusion to need to occur, so no mutual exclusion of any kind was implemented in the main loop thus limiting waiting.

This approach uses load balancing to evenly distribute the work among each thread.
- Load balancing works for this approach as each thread is only doing a constant time operation each number they're considering (in finding all the multiples of a given number).

## EXPERIEMENTAL EVALUATION
INPUT: OUTPUT
- 10<sup>8</sup>: 574.2109 ms 5761455 279209790387276 99999787 99999821 99999827 99999839 99999847 99999931 99999941 99999959 99999971 99999989 
- 10<sup>6</sup>: 36.3646 ms 78498 37550402023 999863 999883 999907 999917 999931 999953 999959 999961 999979 999983
- 10<sup>7</sup>: 47.4513 ms 664579 3203324994356 9999889 9999901 9999907 9999929 9999931 9999937 9999943 9999971 9999973 9999991  
- 999863: 34.0426 ms 78489 37541402550 999683 999721 999727 999749 999763 999769 999773 999809 999853 999863 
  - End range with large prime number
- 1: 1.358 ms 0 0 0 0 0 0 0 0 0 0 0 0 
  - The smallest range
- 2: 1.596 ms 1 2 0 0 0 0 0 0 0 0 0 2
  - Smallest range with at least 1 prime
- 50176014: 222.0149 ms 3011033 73115372139256 50175863 50175871 50175911 50175919 50175953 50175967 50175971 50175973 50175989 50175997 
  - A random number picked out of a hat
