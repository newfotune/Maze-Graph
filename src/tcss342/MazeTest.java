package tcss342;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tcss342.MazeGraph.Location;

public class MazeTest
{
	private MazeGraph maze;

	@Before
	public void setUp() throws Exception
	{
		maze = new MazeGraph(17, 9);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor()
	{
		MazeGraph m = new MazeGraph(4, 4);
	}

	@Test
	public void test()
	{
		
	}

}
