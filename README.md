# HarrisGraphChecker
Author: Akshay Anand
7/19/22

A Harris Graph is a graph that satisfies these 3 conditions:
Eulerian Condition: The graph must contain a Eulerian cycle
non-Hamiltonian Condition: The graph must not contain any Hamiltonian cycles
G-S Condition: For any number of vertices removed from the graph, S, the number of components of the resulting graph after the removal must have a number of components <= S.

This Harris Graph Checker contains a Graphical Interface that allows for the user to plot the graph and check if it is a Harris Graph. When running the main program file (Main.java), two windows will pop up: one window to plot the graph, and the other is an options Menu. The options menu consists of 5 buttons. The first 3 buttons specifies mode. The vertex drawing/deleting mode gives the user the ability to place a vertex by left clicking on the spot to plot it on the graph plotting window. Right clicking an already existing vertex in this mode will delete it. The edge drawing/deleting mode allows for the user to draw an edge between two distinct vertices by dragging from one vertex to another. Right clicking on an edge will delete it. The vertex moving mode allows for the user to move vertices around the screen by dragging a vertex to wherever they want it to move to. The clear button will clear the graph plotting window of all the parts of the existing graph. The check Harris graph button runs the current plotted graph through the harris graph algorithm and prints the results in the standard output terminal.
 
