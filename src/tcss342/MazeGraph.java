package tcss342;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.List;
import java.util.Queue;

/**
 * 
 * @author Chiemeziem Fortune Nwoke (n4tunec@uw.edu)
 * @version 1.0
 *
 */
public class MazeGraph
{
	public static class Location
	{
		public int row;
		public int column;
		//the distance of each node from the start is set to the maximum at first.
		public double distance = Double.POSITIVE_INFINITY;// the distance of
															// each node from
															// the start
		public Location prev;

		public Location(int row, int column)
		{
			this.row = row;
			this.column = column;
		}

		// if the location is valid, return true.
		public boolean isValid()
		{

			return !(row < 0 || column < 0);
		}

		@Override
		public boolean equals(Object theOther)
		{
			Location other = (Location) theOther;

			return (other.row == row && other.column == column);
		}

		@Override
		public String toString()
		{
			return "(" + row + "," + column + ")";
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(row, column);
		}
	}

	private boolean[][][][] adjacent;
	private List<Location> myPath;
	private Queue<Location> myQ;
	private int max_X;
	private int max_Y;

	// Constructs a new maze with size (width, height)
	public MazeGraph(int width, int height)
	{
		myQ = new LinkedList<>();
		myPath = new ArrayList<>();

		if (width < 1 || height < 1 || width % 2 != 1 || height % 2 != 1)
			throw new IllegalArgumentException();

		adjacent = new boolean[height][width][height][width];

		max_X = width;
		max_Y = height;

	}

	/**
	 * Connects two locations together.
	 * 
	 * @param a
	 *            first location.
	 * @param b
	 *            second location.
	 */
	public void connect(Location a, Location b)
	{
		if (a == null || b == null)
			throw new IllegalArgumentException("No Null Locations allowed");
		else if ((a.row == b.row && Math.abs(a.column - b.column) == 1)
				|| (a.column == b.column && Math.abs(a.row - b.row) == 1))
		{
			adjacent[a.row][a.column][b.row][b.column] = true;
			adjacent[b.row][b.column][a.row][a.column] = true;
		} else
		{
			throw new IllegalArgumentException(
					"Locations can only connect to other locations one step beyond it.");
		}
	}

	/**
	 * Gets the shortest path between two locations.
	 * 
	 * @param start
	 *            the starting location.
	 * @param end
	 *            the destination.
	 * @return an arraylist off all locations in the shortest path, from the
	 *         start to the destination.
	 */
	public ArrayList<Location> getShortestPath(Location start, Location end)
	{
		boolean found = false;
		reset();
		//add the start location to the list.
		myPath.add(start);

		if (start == null || end == null)
			throw new IllegalArgumentException("No null locations allowed");

		else if (!start.isValid() || !end.isValid() || !isValidLocation(end)
				|| !isValidLocation(start))
			throw new IllegalArgumentException("Invalid location");

		else if (start.equals(end))
			return (ArrayList<Location>) myPath;

		// Breadth first search
		else
		{
			//add the start to the queue.
			myQ.add(start);
			//distance from start to start = 0.
			start.distance = 0;
			//while the queue is empty, keep searching for the destination.
			while (!myQ.isEmpty())
			{
				//remove from the top of teh queue.
				Location startNode = myQ.remove();
				//get all the neighbors of v
				List<Location> allNeighbors = getAllPossibleConnections(startNode);
				//loop through all possible connection.
				for (int i = 0; i < allNeighbors.size(); i++)
				{
					//get the location in the array.
					Location location = allNeighbors.get(i);
					//add it to the list of paths.
					myPath.add(location);
					//if the location is unchecked.
					if (location.distance == Double.POSITIVE_INFINITY)
					{
						//increase the locations distance from the start by one.
						location.distance = startNode.distance + 1;
						location.prev = startNode;
						//add the location to the queue.
						myQ.add(location);
					}
					//if we found our destination.
					if (location.equals(end))
					{
						found = true;
						//return a list of the paths to the location.
						return printPath(start, location);
					}
				}
			}
		}

		if (!found)
		{
			myPath.clear();
			return (ArrayList<Location>) myPath;
		}

		return (ArrayList<Location>) myPath;
	}

	//reset the list and queue for the next query.
	private void reset()
	{

		myPath.clear();
		myQ.clear();
	}
	//return the list of path by filpping the queue.
	private ArrayList<Location> printPath(Location start, Location w)
	{
		Deque<Location> d = new ArrayDeque<>();
		List<Location> l = new ArrayList<>();

		while (w.prev != null)
		{
			d.push(w);
			w = w.prev;
		}
		d.push(w);

		while (!d.isEmpty())
		{
			l.add(d.pop());
		}

		return (ArrayList<Location>) l;
	}

	/**
	 * Gets all the neighbors from the current location.
	 * 
	 * @param theLoc
	 *            the current location.
	 * @return a list of all the possible locations.
	 */
	private List<Location> getAllPossibleConnections(Location theLoc)
	{
		final List<Location> m = new ArrayList<>();
		int x = theLoc.row, y = theLoc.column;

		Location loc3 = new Location(x, y - 1);
		if (loc3.isValid() && isValidLocation(loc3) && adjacent[x][y][x][y - 1]
				&& !myPath.contains(loc3))
			m.add(loc3);

		Location loc4 = new Location(x, y + 1);
		if (loc4.isValid() && isValidLocation(loc4) && adjacent[x][y][x][y + 1]
				&& !myPath.contains(loc4))
			m.add(loc4);

		Location loc5 = new Location(x - 1, y);
		if (loc5.isValid() && isValidLocation(loc5) && adjacent[x][y][x - 1][y]
				&& !myPath.contains(loc5))
			m.add(loc5);

		Location loc6 = new Location(x + 1, y);
		if (loc6.isValid() && isValidLocation(loc6) && adjacent[x][y][x + 1][y]
				&& !myPath.contains(loc6))
			m.add(loc6);

		return m;
	}

	public boolean isValidLocation(Location theLocation)
	{

		return (theLocation.row < max_X && theLocation.column < max_Y);
	}

	/**
	 * Returns a string representation of the graph.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		// loop through the height
		for (int i = 0; i < max_Y; i++)
		{
			// loop through the width
			for (int j = 0; j < max_X; j++)
			{
				if (i == 0 || j == 0 || j == max_X - 1 || i == max_Y - 1)
				{
					// top left corner
					if (i == 0 && j == 0)
					{
						if (adjacent[i][j][i + 1][j]
								|| adjacent[i][j][i][j + 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// bottom left corner
					else if (i == 0 && j == max_X - 1)
					{
						if (adjacent[i][j][i + 1][j]
								|| adjacent[i][j][i][j - 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// top right corner
					else if (i == max_Y - 1 && j == 0)
					{
						if (adjacent[i][j][i - 1][j]
								|| adjacent[i][j][i][j + 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// bottom right corner
					else if (i == max_Y - 1 && j == max_X - 1)
					{
						if (adjacent[i][j][i - 1][j]
								|| adjacent[i][j][i][j - 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// top wall
					else if (i == 0)
					{
						if (adjacent[i][j][i + 1][j]
								|| adjacent[i][j][i][j + 1]
								|| adjacent[i][j][i][j - 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// bottom wall
					else if (i == max_Y - 1)
					{
						if (adjacent[i][j][i - 1][j]
								|| adjacent[i][j][i][j + 1]
								|| adjacent[i][j][i][j - 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// left wall
					else if (j == 0)
					{
						if (adjacent[i][j][i + 1][j]
								|| adjacent[i][j][i][j + 1]
								|| adjacent[i][j][i - 1][j])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
					// right wall
					else if (j == max_X - 1)
					{
						if (adjacent[i][j][i + 1][j]
								|| adjacent[i][j][i + 1][j]
								|| adjacent[i][j][i][j - 1])
						{
							sb.append(".");
						} else
						{
							sb.append("X");
						}
					}
				}

				else if (adjacent[i][j][i + 1][j] || adjacent[i][j][i - 1][j]
						|| adjacent[i][j][i][j + 1] || adjacent[i][j][i][j - 1])
				{
					sb.append(".");
				} else
				{
					sb.append("X");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String... args)
	{
//		 final int edges[][] = { { 1, 0, 1, 1 }, { 1, 1, 1, 2 }, { 1, 1, 2, 1
//		 },
//		 { 1, 2, 1, 3 }, { 1, 3, 1, 4 }, { 1, 4, 1, 5 }, { 1, 5, 1, 6 },
//		 { 1, 5, 2, 5 }, { 1, 6, 1, 7 }, { 1, 7, 1, 8 }, { 1, 8, 1, 9 },
//		 { 1, 9, 2, 9 }, { 1, 11, 2, 11 }, { 2, 1, 3, 1 },
//		 { 2, 5, 3, 5 }, { 2, 9, 3, 9 }, { 2, 11, 3, 11 },
//		 { 3, 1, 4, 1 }, { 3, 3, 4, 3 }, { 3, 5, 3, 6 }, { 3, 6, 3, 7 },
//		 { 3, 7, 4, 7 }, { 3, 11, 4, 11 }, { 4, 1, 5, 1 },
//		 { 4, 3, 5, 3 }, { 4, 7, 5, 7 }, { 4, 11, 5, 11 },
//		 { 5, 1, 5, 2 }, { 5, 2, 5, 3 }, { 5, 5, 5, 6 }, { 5, 6, 5, 7 },
//		 { 5, 7, 5, 8 }, { 5, 8, 5, 9 }, { 5, 11, 5, 12 } };
//		
//		 MazeGraph maze = new MazeGraph(13, 7);
//		 for (int[] edge : edges)
//		 maze.connect(new Location(edge[0], edge[1]), new Location(edge[2],
//		 edge[3]));
//		 System.out.println(maze);
//		 System.out.println(maze.getShortestPath(new Location(2, 2), new
//		 Location(3, 3)));

		MazeGraph maze = new MazeGraph(3, 3);
		maze.connect(new Location(0, 0), new Location(0, 1));
		maze.connect(new Location(0, 0), new Location(1, 0));

		maze.connect(new Location(0, 1), new Location(0, 0));
		maze.connect(new Location(0, 1), new Location(0, 2));
		maze.connect(new Location(0, 1), new Location(1, 1));

		maze.connect(new Location(0, 2), new Location(0, 1));
		maze.connect(new Location(0, 2), new Location(1, 2));

		maze.connect(new Location(1, 0), new Location(0, 0));
		maze.connect(new Location(1, 0), new Location(1, 1));
		maze.connect(new Location(1, 0), new Location(2, 0));

		maze.connect(new Location(1, 1), new Location(0, 1));
		maze.connect(new Location(1, 1), new Location(1, 0));
		maze.connect(new Location(1, 1), new Location(1, 2));
		maze.connect(new Location(1, 1), new Location(2, 1));

		maze.connect(new Location(1, 2), new Location(0, 2));
		maze.connect(new Location(1, 2), new Location(1, 1));
		maze.connect(new Location(1, 2), new Location(2, 2));

		maze.connect(new Location(2, 0), new Location(1, 0));
		maze.connect(new Location(2, 0), new Location(2, 1));

		maze.connect(new Location(2, 1), new Location(1, 1));
		maze.connect(new Location(2, 1), new Location(2, 0));
		maze.connect(new Location(2, 1), new Location(2, 2));

		maze.connect(new Location(2, 2), new Location(1, 2));
		maze.connect(new Location(2, 2), new Location(2, 1));

		System.out.println(maze);
		// different locations
		 System.out.println(maze.getShortestPath(new Location(0, 0), new
		 Location(2, 2)));
		// System.out.println(maze.getAllPossibleConnections(new Location(1, 1)));
		 
//		 System.out.println(maze.getShortestPath(new Location(0, 0), new
//				 Location(2, 0)));
//		 System.out.println(maze.getShortestPath(new Location(0, 0), new
//				 Location(0, 0)));
		 
	
		// null parameters
		// System.out.println(maze.getShortestPath(null, null));

		// same locations
//		System.out.println(maze.getShortestPath(new Location(0, 0),
//				new Location(0, 0)));
		//
		// MazeGraph A = new MazeGraph(9,9);
		// A.connect(new MazeGraph.Location(2, 3), new MazeGraph.Location(3,
		// 3));
		// A.connect(new MazeGraph.Location(2, 2), new MazeGraph.Location(2,
		// 3));
		// A.connect(new MazeGraph.Location(1, 3), new MazeGraph.Location(2,
		// 3));
		// A.connect(new MazeGraph.Location(2, 4), new MazeGraph.Location(2,
		// 3));
		//
		// System.out.println(A.toString());
		// System.out.println(A.getShortestPath(new Location(2, 2), new
		// Location(1, 3)));
		//
	}
}