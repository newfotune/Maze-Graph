# Maze-Graph
This is the back end of a maze program. 

The map is represented with a 4d boolean array. This is an undirected graph.
From any point in the graph, one can only move one point up, down, left or right. 
One cannot move out of the graph matrix, so boundry points will be unable to move in all 4 locations.
The shortest path is constructed using the breadth -first search algorithm
