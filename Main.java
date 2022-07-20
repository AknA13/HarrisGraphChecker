/*
Made by Akshay Anand, 2022
NOTE: made a lot of the unused arrays null to free up memory because the JVM garbage collector wasn't doing so
INPUT DETAILS:
  Line one: an integer (n) specifying the order of the graph
  For every vertex (0 to n-1 inclusive):
  Line # 1: Specify the degree of the vertex, Line #2: space separated integers specifying which vertices from 0 to n-1 are adjacent to this vertex
EXAMPLE (entering a complete graph of order 4):
  4
  3
  1 2 3
  3
  2 3 0
  3
  3 0 1
  3
  0 1 2
Time Complexity:
  Eulerian Check: O(N)
  Hamiltonian Check: O(N^2)
  G/S Check: O(N^2 * 2^N)
*/
import java.util.*;
class Main{
  public static int N;
  public static boolean eulerian;
  public static boolean tough;
  public static boolean hamiltonian;
  public static void main(String[] args) {
    //Input parsing
    Scanner in = new Scanner(System.in);
    ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
    N = in.nextInt();
     for(int i = 0 ; i < N; i ++){
      in.nextLine();
       int j = in.nextInt();
       in.nextLine();
      ArrayList<Integer> tmp = new ArrayList<Integer>();
      for(int k = 0 ; k < j ; k++){
         tmp.add(in.nextInt());
      }
      graph.add(tmp);
       tmp = null;
     }
    //^Stored graph in a modified adjacency matrix
    eulerian = true;
    checkEulerian(graph);
    System.out.println("Eulerian: "+eulerian);
    hamiltonian = false;
    checkHamiltonian(0,new ArrayList<Integer>(), graph,0);
    System.out.println("Hamiltonian: "+ hamiltonian);
    tough = true;
    checkToughness(graph, new ArrayList<Integer>(), 0);
    System.out.println("Tough: " + tough);
    
    if(!hamiltonian && tough && eulerian){
      System.out.println("This is a Harris Graph");
    }else{
      System.out.println("This isn't a Harris Graph");
    }
    in.close();
  }

  //checks if every vertex has an even degree
  public static void checkEulerian(ArrayList<ArrayList<Integer>> graph){
    for(int i = 0 ; i < graph.size(); i ++){
      if(graph.get(i).size()%2 !=0){
        eulerian = false;
        break;
      }
    }
  }
  
  //DFS algorithm that searches for a hamiltonian cycle
  public static void checkHamiltonian(int vertex, ArrayList<Integer> points_visited, ArrayList<ArrayList<Integer>> graph, int target){
    if(vertex == target && points_visited.size() == graph.size()){
      hamiltonian = true;
      return;
    }
    for(int i = 0; i < points_visited.size(); i ++){
      if(points_visited.get(i) == vertex){
        return;
      }
    }
    for(int i = 0 ; i < graph.get(vertex).size(); i ++){
      ArrayList<Integer> tmp = new ArrayList<Integer>();
      for(int j = 0 ; j < points_visited.size(); j++){
        tmp.add(points_visited.get(j));
      }
      tmp.add(vertex);
      checkHamiltonian(graph.get(vertex).get(i), tmp, graph, target);
      tmp = null;
    }
  }
  //recursive algorithm that checks if every possible combination of vertices removed results in more components of the graph created
  public static void checkToughness(ArrayList<ArrayList<Integer>> graph, ArrayList<Integer> set, int num){
    if(graph.size() == num){
        int comps = getNumComponents(graph, new ArrayList<ArrayList<Integer>>(),set);
        if(comps>set.size() && set.size()>0){
          tough = false;
          return;
        }
    }else{
      ArrayList<Integer> tmp1 = new ArrayList<Integer>();
      for(int i = 0 ; i < set.size(); i ++){
        tmp1.add(set.get(i));
      }
      tmp1.add(num);
      checkToughness(graph, tmp1, num + 1);
      tmp1 = null;
      ArrayList<Integer> tmp2 = new ArrayList<Integer>();
      for(int i = 0 ; i < set.size(); i ++){
        tmp2.add(set.get(i));
      }
      checkToughness(graph, tmp2, num + 1);
      tmp2 = null;
    }
  }
  
//gives the number of components in a graph
  public static int getNumComponents(ArrayList<ArrayList<Integer>> graph,ArrayList<ArrayList<Integer>> pieces,ArrayList<Integer> set){
    ArrayList<Integer> pointsLeft = new ArrayList<Integer>();
    for(int i = 0 ; i < N; i ++){
      boolean check = true;
      for(int k = 0 ; k < set.size(); k ++){
        if(set.get(k)==i){
          check = false;
          break;
        }
      }
      if(check){
        pointsLeft.add(i);
      }
    }
    while(pointsLeft.size()>0){
      ArrayList<Integer> group = new ArrayList<Integer>();
      findAllPossibleVisitedPoints(pointsLeft.get(0),graph, group, set);
      pieces.add(group);
      for(int i = 0 ; i < group.size(); i ++){
        for(int j = 0 ; j < pointsLeft.size(); j ++){
          if(group.get(i)==pointsLeft.get(j)){
            pointsLeft.remove(j);
            j--;
          }        
        }
      }
      group = null;
    }
    pointsLeft = null;
    return pieces.size();
  }
  
  //finds all possible points reachable from one given starting point - helper method for getNumComponents()
  public static void findAllPossibleVisitedPoints(int vertex, ArrayList<ArrayList<Integer>> graph, ArrayList<Integer> visited, ArrayList<Integer> set){
    for(int i = 0 ; i < visited.size(); i ++){
      if(vertex == visited.get(i)){
        return;
      }
    }
    visited.add(vertex);
    for(int i = 0 ; i < graph.get(vertex).size(); i ++){
      boolean inK = false;
      for(int j = 0 ; j < set.size();j ++){
        if(graph.get(vertex).get(i)==set.get(j)){
          inK = true;
          break;
        }
      }
      if(!inK){
        findAllPossibleVisitedPoints(graph.get(vertex).get(i), graph, visited, set);
      }
    }
  }
}