import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class KruskalAlgorithm
{
    private List<Edge1> edges;
    private int numberOfVertices;
    public static final int MAX_VALUE = 999;
    private int visited[];
    private double[][] spanning_tree;

    public KruskalAlgorithm(int numberOfVertices)
    {
        this.numberOfVertices = numberOfVertices;
        edges = new LinkedList<Edge1>();
        visited = new int[this.numberOfVertices + 1];
        spanning_tree = new double[numberOfVertices + 1][numberOfVertices + 1];
    }

    public void kruskalAlgorithm(double[][] adjacencyMatrix)
    {
        boolean finished = false;
        for (int source = 0; source < numberOfVertices; source++)
        {
            for (int destination = 0; destination < numberOfVertices; destination++)

            {
                if (adjacencyMatrix[source][destination] != 0.0 && source != destination)
                {
                    Edge1 edge = new Edge1();
                    edge.sourcevertex = source;
                    edge.destinationvertex = destination;
                    edge.weight = adjacencyMatrix[source][destination];
                    adjacencyMatrix[destination][source] = MAX_VALUE;
                    edges.add(edge);
                }
            }
        }
        Collections.sort(edges, new Edge1Comparator());
        CheckCycle checkCycle = new CheckCycle();
        for (Edge1 edge : edges)
        {
            spanning_tree[edge.sourcevertex][edge.destinationvertex] = edge.weight;
            spanning_tree[edge.destinationvertex][edge.sourcevertex] = edge.weight;
            if (checkCycle.checkCycle(spanning_tree, edge.sourcevertex))
            {
                spanning_tree[edge.sourcevertex][edge.destinationvertex] = 0;
                spanning_tree[edge.destinationvertex][edge.sourcevertex] = 0;
                edge.weight = -1;
                continue;
            }
            visited[edge.sourcevertex] = 1;
            visited[edge.destinationvertex] = 1;
            for (int i = 0; i < visited.length; i++)
            {
                if (visited[i] == 0)
                {
                    finished = false;
                    break;
                } else
                {
                    finished = true;
                }
            }
            if (finished)
                break;
        }
        System.out.println("The spanning tree is ");
        for (int i = 1; i <= numberOfVertices; i++)
            System.out.print("\t" + i);
        System.out.println();
        for (int source = 1; source <= numberOfVertices; source++)
        {
            System.out.print(source + "\t");
            for (int destination = 1; destination <= numberOfVertices; destination++)
            {
                System.out.print(spanning_tree[source][destination] + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String... arg)
    {
       double[][] adjacency_matrix = new double[5][5];
        adjacency_matrix[1][1] = -1;
        adjacency_matrix[1][2] = 3.0;
        adjacency_matrix[1][3] = 4.2;
        adjacency_matrix[1][4] = 5.0;
        adjacency_matrix[2][2] = -1;
        adjacency_matrix[2][3] = 6.0;
        adjacency_matrix[2][4] = 2.0;
        adjacency_matrix[2][1] = 3.0;
        adjacency_matrix[3][3] = -1;
        adjacency_matrix[3][1] = 4.2;
        adjacency_matrix[3][2] = 6.0;
        adjacency_matrix[3][4] = 1.3;
        adjacency_matrix[4][4] = -1;
        adjacency_matrix[4][3] = 1.3;
        adjacency_matrix[4][2] = 2.0;
        adjacency_matrix[4][1] = 5.0;
        int number_of_vertices = 4;

//        for (int i = 1; i <= number_of_vertices; i++)
//        {
//            for (int j = 1; j <= number_of_vertices; j++)
//            {
//                adjacency_matrix[i][j] = scan.Double();
//                if (i == j)
//                {
//                    adjacency_matrix[i][j] = 0;
//                    continue;
//                }
//                if (adjacency_matrix[i][j] == 0)
//                {
//                    adjacency_matrix[i][j] = MAX_VALUE;
//                }
//            }
//        }




        KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm(number_of_vertices);
        kruskalAlgorithm.kruskalAlgorithm(adjacency_matrix);
    }
}

class Edge1
{
    int sourcevertex;
    int destinationvertex;
    double weight;
}

class Edge1Comparator implements Comparator<Edge1>
{
    @Override
    public int compare(Edge1 edge1, Edge1 edge2)
    {
        if (edge1.weight < edge2.weight)
            return -1;
        if (edge1.weight > edge2.weight)
            return 1;
        return 0;
    }
}

class CheckCycle
{
    private Stack<Integer> stack;
    private double[][] adjacencyMatrix;

    public CheckCycle()
    {
        stack = new Stack<Integer>();
    }

    public boolean checkCycle(double[][] adjacency_matrix, int source)
    {
        boolean cyclepresent = false;
        int number_of_nodes = adjacency_matrix[source].length - 1;

        adjacencyMatrix = new double[number_of_nodes + 1][number_of_nodes + 1];
        for (int sourcevertex = 1; sourcevertex <= number_of_nodes; sourcevertex++)
        {
            for (int destinationvertex = 1; destinationvertex <= number_of_nodes; destinationvertex++)
            {
                adjacencyMatrix[sourcevertex][destinationvertex] = adjacency_matrix[sourcevertex][destinationvertex];
            }
        }

        int visited[] = new int[number_of_nodes + 1];
        int element = source;
        int i = source;
        visited[source] = 1;
        stack.push(source);

        while (!stack.isEmpty())
        {
            element = stack.peek();
            i = element;
            while (i <= number_of_nodes)
            {
                if (adjacencyMatrix[element][i] >= 1 && visited[i] == 1)
                {
                    if (stack.contains(i))
                    {
                        cyclepresent = true;
                        return cyclepresent;
                    }
                }
                if (adjacencyMatrix[element][i] >= 1 && visited[i] == 0)
                {
                    stack.push(i);
                    visited[i] = 1;
                    adjacencyMatrix[element][i] = 0;// mark as labelled;
                    adjacencyMatrix[i][element] = 0;
                    element = i;
                    i = 1;
                    continue;
                }
                i++;
            }
            stack.pop();
        }
        return cyclepresent;
    }
}