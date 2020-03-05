import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * All Pairs Shortest Path Algorithm
 * Also known as Floyd-Warshall Algorithm
 * @author Deron Washington II
 *
 */
public class APSP
{

	/**
	 * Adds a matrix multiplication style (rows by column) by finding the 
	 * minimum between i and j (rows and columns) to create the resulting
	 * data structure which will be used to create the giant one
	 * @param dij_k = the shortest distance from i to j in exactly k - 1 steps (used when k > 1)
	 * @param adjMatrix = adjacency matrix holding all the distances between the vertices
	 * @return 
	 * 				= the resulting minimum addition matrix representing the shortest path between
	 *				    two vertices
	 */
	public static ArrayList<Integer[][]> minAdditionMatrix(Integer[][] dij_k, Integer[][] adjMatrix)
	{
		int min = Integer.MAX_VALUE;
		int sum = Integer.MAX_VALUE;
		int arrSize = dij_k.length;
		int whereMin = 0;
		Integer[][] sumArr = new Integer[arrSize][arrSize];
		
		// each value in this array holds the vertex that held the minimum path
		Integer[][] minLocations = new Integer[arrSize][arrSize];
		
		// holds both the sumArr n minLocations arrays
		ArrayList<Integer[][]> sumArrNMinLoc = new ArrayList<Integer[][]>();
		

		for (int iRow = 0; iRow < arrSize; iRow++)
		{
			for (int finalCol = 0; finalCol < arrSize; finalCol++)
			{
				// reset
				min = Integer.MAX_VALUE;
				sum = Integer.MAX_VALUE;
				
				for (int iCol = 0; iCol < arrSize; iCol++)
				{	
//					// if true can't be a minimum
//					if (dij_k[iRow][iCol] == Integer.MAX_VALUE || adjMatrix[iCol][finalCol] == Integer.MAX_VALUE)
//						continue;
					
					// compute the sum (multiplication style) and find the minimum
					if (dij_k[iRow][iCol] != Integer.MAX_VALUE && adjMatrix[iCol][finalCol] != Integer.MAX_VALUE)	
					{
						sum = dij_k[iRow][iCol] + adjMatrix[iCol][finalCol];
						min = Integer.min(sum, min);
						whereMin = iCol;
					}
					
					// assign minimum to sumArr if at the end of the iCol
					if (iCol == arrSize - 1)
						{
							sumArr[iRow][finalCol] = min;
							minLocations[iRow][finalCol] = whereMin;
						}
					
					// if min = 0 go to next value in the sumArr cuz min can't be < 0
					else if (min == 0)
					{
						sumArr[iRow][finalCol] = min;
						minLocations[iRow][finalCol] = whereMin;
						break;
					}
				}
			}
		}
		
		sumArrNMinLoc.add(sumArr);
		sumArrNMinLoc.add(minLocations);
		
		return sumArrNMinLoc;
	}

	/**
	 * Creates the data structure to be used to query for the shortest distance between two
	 * points
	 * @param adjMatrix = adjacency matrix holding all the distances between the vertices
	 * @return
	 * 				= the resulting data structure to give us the shortest distance between two vertices
	 */
	public static ArrayList<Integer[][]> createDataStructure(Integer[][] adjMatrix)	
	{
		// max num of steps without repeating the step currently on
		int maxSteps = adjMatrix.length - 1;
		int i = 0;
		//int j = maxSteps;

		// should be of (2 * size maxSteps) - 1 at the end 
		//( cuz one array for structure and one for minLocations for each step)
		ArrayList<Integer[][]> dataStructure = new ArrayList<Integer[][]>();

		// k = numOfSteps to get from i to j
		for (int k = 1; k <= maxSteps; k++)
		{
			if (k == 1)
				dataStructure.add(adjMatrix);

			else
			{
				// GENERAL
				// find the smallest distance from i to j using k steps + weight from last step (doesn't change)
				// SPECIFIC
				// so for k == 2 
				// find the smallest distance from i to j using 1 step + weight from last step (doesn't change)
				dataStructure.addAll(minAdditionMatrix(dataStructure.get(i - 1), dataStructure.get(0)));

			}
			
			i++;
		}

		return dataStructure;
	}

	/**
	 * Find the minimum path from any two vertices and return a string representation
	 * @param dataStructure = dataStructure that we generated earlier to hold
	 * 										 all the information we need to calculate the shortest
	 * 										 distance between two points
	 * @param sVertex 		  = starting vertex
	 * @param eVertex		  = ending vertex
	 * @return
	 * 									  = string representation of the path
	 */
	public static String findMinPath(ArrayList<Integer[][]> dataStructure, int sVertex, int eVertex)
	{			
		String path = "";
		int i = sVertex - 1;
		int j = eVertex - 1;
		
		if (i < 0 || j >= dataStructure.size())
			return new String("Incorrect vertices. Try inputting valid vertices next time.");
		
		int sIndex = dataStructure.size() - 1;
		int pathDist = dataStructure.get(sIndex - 1)[i][j];
		
		do
		{
			j = dataStructure.get(sIndex)[i][j];
			path = j + "->" + path.substring(0);
			sIndex -= 2;
		} while (sIndex != 0);
		
		path = sVertex + "->" + path.substring(0) + eVertex + "= " + pathDist;
		
		return path;
	}
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		// FILL OUT RANDOMLY GENERATED ADJACENCY MATRIX
		Random random = new Random();
		int num = -100;
		//Integer[][] adjacencyMatrix = new Integer[4][4];
		int max =  Integer.MAX_VALUE;

//		for (int i = 0; i < 4; i++)
//		{
//			for(int j = 0; j < 4; j++)
//			{
//				num = random.nextInt(11); // 0-10 (10 means impossible therefore infinity)
//
//				if (num != 10)
//					adjacencyMatrix[i][j] = num;
//				else
//					adjacencyMatrix[i][j] = Integer.MAX_VALUE;
//
//			}
//		}
		
		// hardcoded from example
		Integer[][] adjacencyMatrix = {
				{0, 5, 9, max},
				{max, 0, 1, max},
				{max, max, 0, 2},
				{max, 3, max, 0}};

		// FIND SHORTEST DISTANCE FROM ANY VERTEX TO ANY OTHER VERTEX
		ArrayList<Integer[][]> dataStructure = new ArrayList<Integer[][]>();
		dataStructure = createDataStructure(adjacencyMatrix);
		
		System.out.print("Between what two points would you like to find the shortest path?"
				+ "\nInput two numbers seperated by a space.\n\n");
		
		Scanner scan = new Scanner(System.in);
		
		int startingVertex = scan.nextInt()	;
		int endingVertex = scan.nextInt();
		
		
		System.out.printf( "%n%nThe shortest path between %d and %d is: %s",
				startingVertex, endingVertex, findMinPath(dataStructure, startingVertex, endingVertex));
		
		scan.close();

	}
}
