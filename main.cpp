/*
Made by Akshay Anand, MMSS 2022
NOTE: Next step is to utilize Dynamic Programming and store already discovered paths when doing DFS in order to be available for G/S check. Store all Eulerian paths. 
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
#include <iostream>
#include <vector>
#include <string>
#include  <bits/stdc++.h>
#include <chrono>
using namespace std; 
int N;
bool eulerian;
bool tough;
bool hamiltonian;
// checks if every vertex has an even degree
void checkEulerian(std::vector<std::vector<int>> &graph)
{
  for (int i = 0; i < graph.size(); i++)
  {
    if (graph[i].size() % 2 != 0)
    {
      eulerian = false;
      break;
    }
  }
}
// DFS algorithm that searches for a hamiltonian cycle
void checkHamiltonian(int vertex, std::vector<int> &points_visited, std::vector<std::vector<int>> &graph, int target)
{
  if (vertex == target && points_visited.size() == graph.size())
  {
    hamiltonian = true;
    return;
  }
  for (int i = 0; i < points_visited.size(); i++)
  {
    if (points_visited[i] == vertex)
    {
      return;
    }
  }
  for (int i = 0; i < graph[vertex].size(); i++)
  {
  std::vector<int> tmp = std::vector<int>();
  for (int j = 0; j < points_visited.size(); j++)
  {
    tmp.push_back(points_visited[j]);
  }
  tmp.push_back(vertex);
  checkHamiltonian(graph[vertex][i], tmp, graph, target);
  }
}
// finds all possible points reachable from one given starting point - helper method for getNumComponents()
void findAllPossibleVisitedPoints(int vertex, std::vector<std::vector<int>> &graph, std::vector<int> &visited, std::vector<int> &set)
{
  for (int i = 0; i < visited.size(); i++)
  {
    if (vertex == visited[i])
    {
      return;
    }
  }
  visited.push_back(vertex);
  for (int i = 0; i < graph[vertex].size(); i++)
  {
    bool inK = false;
    for (int j = 0; j < set.size(); j++)
    {
      if (graph[vertex][i] == set[j])
      {
        inK = true;
        break;
      }
    }
    if (!inK)
    {
      findAllPossibleVisitedPoints(graph[vertex][i], graph, visited, set);
    }
  }
}
// gives the number of components in a graph
int getNumComponents(std::vector<std::vector<int>> &graph, std::vector<int> &set)
{
  std::vector<int> pointsLeft = std::vector<int>();
  for (int i = 0; i < N; i++)
  {
    bool check = true;
    for (int k = 0; k < set.size(); k++)
    {
      if (set[k] == i)
      {
        check = false;
        break;
      }
    }
    if (check)
    {
      pointsLeft.push_back(i);
    }
  }
         
  int numComps = 0;
  
  while (pointsLeft.size() > 0)
  {
    std::vector<int> group = std::vector<int>();           
    findAllPossibleVisitedPoints(pointsLeft[0], graph, group, set);
    numComps++;
    for (int i = 0; i < group.size(); i++)
    {
      for (int j = 0; j < pointsLeft.size(); j++)
      {
        if (group[i] == pointsLeft[j])
        {
          pointsLeft.erase(pointsLeft.begin() + (j));
          j--;
        }
      }
    }
  }
  return numComps;
}
// recursive algorithm that checks if every possible combination of vertices removed results in more components of the graph created
void checkToughness(std::vector<std::vector<int>> &graph, std::vector<int> &set, int num)
{
  if (graph.size() == num)
  {
    int comps = getNumComponents(graph, set);
    if (comps > set.size() && set.size() > 0)
    {
      tough = false;
      return;
    }
  }
  else 
  {
    std::vector<int> tmp1 = std::vector<int>();
    std::vector<int> tmp2 = std::vector<int>();
    for (int i = 0; i < set.size(); i++)
    {
      tmp1.push_back(set[i]);
      tmp2.push_back(set[i]);
    }
    tmp1.push_back(num);
    checkToughness(graph, tmp1, num + 1);
    checkToughness(graph, tmp2, num + 1);
  }
}

int main(){
  cin>>N;
  std::vector<std::vector<int>> graph = std::vector<std::vector<int>>();
  for (int i = 0; i < N; i++)
  {
    int m;
    cin>>m;
    string line;
    getline(cin >> ws,line);
    std::vector<int> vec = std::vector<int>();
    std::stringstream iss(line);
    int num;
    while ( iss >> num ){
      vec.push_back( num );
    }
    graph.push_back(vec);
  }
  
  // ^Stored graph in a modified adjacency list
  eulerian = true;
  checkEulerian(graph);
  string b = eulerian?"true":"false";
  std::cout << "Eulerian: " + b << std::endl;

  
  hamiltonian = false;
  std::vector<int> vec = std::vector<int>(); 
  checkHamiltonian(0,vec,graph,0);
  b = hamiltonian?"true":"false";
  std::cout << "Hamiltonian: " + b << std::endl;
  
  tough = true;
  std::vector<int> vec2 = std::vector<int>();  
  checkToughness(graph,vec2,0);
  b = tough?"true":"false";
  std::cout << "Tough: " + b << std::endl;
  
  if (!hamiltonian && tough && eulerian)
  {
    std::cout << "This is a Harris Graph" << std::endl;
  }
  else 
  {
    std::cout << "This isn\'t a Harris Graph" << std::endl;
  }
  return 0;
}