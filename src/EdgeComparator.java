import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge>
{
    @Override
    public int compare(Edge x, Edge y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.length  < y.length)
        {
            return -1;
        }
        if (x.length > y.length)
        {
            return 1;
        }
        return 0;
    }
}