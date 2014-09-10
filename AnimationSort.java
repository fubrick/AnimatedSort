import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;


/**
 * This program animates sort algorithms.
 * @author Jeremy Jones
 **/
public class AnimationSort
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner (System.in);
      System.out.print("\nEnter a delay (50 recomended): ");
      int delay = in.nextInt();

      System.out.print("Enter size of data (100 recomended): ");
      int dataSize = in.nextInt();
      code that does not work

      ArrayComponent panel = new ArrayComponent();
      JFrame frame = new JFrame();
      frame.setTitle("InsertionSort");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(panel, BorderLayout.CENTER);
      frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      frame.setVisible(true);

      ArrayComponent panel2 = new ArrayComponent();
      JFrame frame2 = new JFrame();
      frame2.setTitle("MergeSort");
      frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame2.add(panel2, BorderLayout.CENTER);
      frame2.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      frame2.setLocation(FRAME_WIDTH, 0);
      frame2.setVisible(true);

      ArrayComponent panel3 = new ArrayComponent();
      JFrame frame3 = new JFrame();
      frame3.setTitle("HeapSort");
      frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame3.add(panel3, BorderLayout.CENTER);
      frame3.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      frame3.setLocation(0, FRAME_HEIGHT + 25);
      frame3.setVisible(true);

      ArrayComponent panel4 = new ArrayComponent();
      JFrame frame4 = new JFrame();
      frame4.setTitle("QuickSort");
      frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame4.add(panel4, BorderLayout.CENTER);
      frame4.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      frame4.setLocation(FRAME_WIDTH, FRAME_HEIGHT + 25);
      frame4.setVisible(true);

      Double[] values = new Double[dataSize];
      for (int i = 0; i < values.length; i++)
         values[i] = Math.random() * panel.getHeight();
      Double[] values2 = values.clone();
      Double[] values3 = values.clone();
      Double[] values4 = values.clone();


      Runnable r1 = new SorterInsert(values, panel, delay);
      Runnable r2 = new SorterMerge(values2, panel2, delay);
      Runnable r3 = new SorterHeap(values3, panel3, delay);
      Runnable r4 = new SorterQuick(values4, panel4, delay);

      Thread t1 = new Thread(r1);
      Thread t2 = new Thread(r2);
      Thread t3 = new Thread(r3);
      Thread t4 = new Thread(r4);

      t1.start();
      t2.start();
      t3.start();
      t4.start();
   }
   private static final int FRAME_WIDTH = 500;
   private static final int FRAME_HEIGHT = 300;
}

/**
 *  This component draws an array and marks two elements in the
 *  array that are being compared.
 **/
class ArrayComponent extends JComponent
{
   // methods must be synchronized because gui component uses its own thread
   public synchronized void paintComponent(Graphics g)
   {
      if (values == null) return;
      Graphics2D g2 = (Graphics2D) g;
      int width = getWidth() / values.length;
      for (int i = 0; i < values.length; i++)
      {
         Double v =  values[i];
         Rectangle2D bar = new Rectangle2D.Double(width * i, 0, width, v);
         if (v == marked1 || v == marked2)
            g2.fill(bar);
         else
            g2.draw(bar);
      }
   }

   /**
    *  Sets the values to be painted.
    *  @param values the array of values to display
    *  @param marked1 the first marked element
    *  @param marked2 the second marked element
    **/
   public synchronized void setValues(Double[] values, Double marked1, Double marked2)
   {
      this.values = (Double[]) values.clone();
      this.marked1 = marked1;
      this.marked2 = marked2;
      repaint();
   }

   private Double[] values;
   private Double marked1;
   private Double marked2;
}

/**
 * class with various sorting algorithms
 **/
class Sorter
{
   /**
    * sorts an array using insertion sort algorithm.
    **/
   public static <E> void insertionSort( E[ ] a, Comparator<? super E> comp )
    {
        int j;

        for( int p = 1; p < a.length; p++ )
        {
            E tmp = a[ p ];
            for( j = p; j > 0 && comp.compare( tmp, a[ j - 1] ) < 0; j-- )
                a[ j ] = a[ j - 1 ];
            a[ j ] = tmp;
        }
    }
    
   /**
    *  Sorts an array, using the merge sort algorithm.
    **/
   public static <E> void mergeSort(E[] a, Comparator<? super E> comp)
   {
      mergeSort(a, 0, a.length - 1, comp);
   }

   /**
    * Sorts a range of an array, using the merge sort
    * algorithm.
    **/
   private static <E> void mergeSort(E[] a, int from, int to, Comparator<? super E> comp)
   {
      if (from == to) return;
      int mid = (from + to) / 2;
      // Sort the first and the second half
      mergeSort(a, from, mid, comp);
      mergeSort(a, mid + 1, to, comp);
      merge(a, from, mid, to, comp);
   }

   /**
    * Merges two adjacent subranges of an array
    */
   private static <E> void merge(E[] a, int from, int mid, int to, Comparator<? super E> comp)
   {
      int n = to - from + 1;
         // Size of the range to be merged

      // Merge both halves into a temporary array b
      Object[] b = new Object[n];

      int i1 = from;
         // Next element to consider in the first range
      int i2 = mid + 1;
         // Next element to consider in the second range
      int j = 0;
         // Next open position in b

      // As long as neither i1 nor i2 past the end, move
      // the smaller element into b
      while (i1 <= mid && i2 <= to)
      {
         if (comp.compare(a[i1], a[i2]) < 0)
         {
            b[j] = a[i1];
            i1++;
         }
         else
         {
            b[j] = a[i2];
            i2++;
         }
         j++;
      }

      // Copy any remaining entries of the first half
      while (i1 <= mid)
      {
         b[j] = a[i1];
         i1++;
         j++;
      }

      // Copy any remaining entries of the second half
      while (i2 <= to)
      {
         b[j] = a[i2];
         i2++;
         j++;
      }

      // Copy back from the temporary array
      for (j = 0; j < n; j++)
         a[from + j] = (E) b[j];
   }

   /**
    * method to represent left child in heap
    * @param i index int
    **/
   private static int leftChild( int i )
   {
     return 2 * i + 1;
   }

   /**
    * method to percolate an element down a heap from
    * a given index in an array to a given index in
    * @param a the array
    * @param i starting index
    * @param n ending index
    * @param comp comparator used to compare elements
    **/
   private static <E> void percDown( E[] a, int i, int n, Comparator<? super E> comp )
   {
     int child;
     E tmp;

     for( tmp = a[ i ]; leftChild( i ) < n; i = child )
     {
         child = leftChild( i );
         if( child != n - 1 && comp.compare( a[ child ], a[ child + 1 ] ) < 0 )
             child++;
         if( comp.compare( tmp, a[ child ] ) < 0 )
             a[ i ] = a[ child ];
         else
             break;
     }
     a[ i ] = tmp;
   }

   /**
    * sort an array using heap sort algorithm
    **/
   public static <E> void heapsort( E[] a, Comparator<? super E> comp )
   {
     for( int i = a.length / 2 - 1; i >= 0; i-- )  /* buildHeap */
         percDown( a, i, a.length, comp );
     for( int i = a.length - 1; i > 0; i-- )
     {
         swapReferences( a, 0, i );                /* deleteMax */
         percDown( a, 0, i, comp );
     }
   }

   /**
    * swaps two elements in a given array
    **/
   public static <E> void swapReferences( E[] a, int index1, int index2 )
   {
     E tmp = a[ index1 ];
     a[ index1 ] = a[ index2 ];
     a[ index2 ] = tmp;
   }


   /**
    * Quicksort algorithm.
    * @param a an array of Comparable items.
    */
   public static <E> void quicksort( E[] a , Comparator<? super E> comp)
   {
      quicksort( a, 0, a.length - 1, comp );
   }

    private static final int CUTOFF = 3;

   /**
    * Return median of left, center, and right.
    * Order these and hide the pivot.
    */
   private static <E> E median3( E[] a, int left, int right, Comparator<? super E> comp )
   {
      int center = ( left + right ) / 2;
         if( comp.compare( a[ center ], a[ left ] ) < 0 )
      swapReferences( a, left, center );
         if( comp.compare( a[ right ], a[ left ] ) < 0 )
      swapReferences( a, left, right );
         if( comp.compare( a[ right ], a[ center ] ) < 0 )
      swapReferences( a, center, right );

         // Place pivot at position right - 1
      swapReferences( a, center, right - 1 );
      return a[ right - 1 ];
   }

   /**
    * Internal quicksort method that makes recursive calls.
    * Uses median-of-three partitioning and a cutoff of 10.
    */
   private static <E> void quicksort( E[] a, int left, int right, Comparator<? super E> comp )
   {
      if( left + CUTOFF <= right )
      {
         E pivot = median3( a, left, right, comp );

             // Begin partitioning
         int i = left, j = right - 1;
         for( ; ; )
         {
            while( comp.compare( a[ ++i ] , pivot ) < 0 ) { }
            while( comp.compare( a[ --j ] , pivot ) > 0 ) { }
            if( i < j )
               swapReferences( a, i, j );
            else
               break;
         }

         swapReferences( a, i, right - 1 );   // Restore pivot

         quicksort( a, left, i - 1, comp );    // Sort small elements
         quicksort( a, i + 1, right, comp );   // Sort large elements
      }
      else
         insertionSort( a, left, right, comp );
   }

   private static <E> void insertionSort( E[] a, int left, int right, Comparator<? super E> comp )
   {
      for( int p = left + 1; p <= right; p++ )
      {
         E tmp = a[ p ];
         int j;

         for( j = p; j > left && comp.compare( tmp, a[ j - 1 ] ) < 0; j-- )
            a[ j ] = a[ j - 1 ];
         a[ j ] = tmp;
      }
   }
}

/**
   This runnable executes a sort algorithm.
   When two elements are compared, the algorithm
   pauses and updates a panel.
*/
class SorterInsert implements Runnable
{
   /**
      Constructs the sorter.
      @param values the array to sort
      @param panel the panel for displaying the array
   */
   public SorterInsert(Double[] values, ArrayComponent panel, int delay)
   {
      this.values = values;
      this.panel = panel;
      this.delay = delay;
   }

   public void run()
   {
      Comparator<Double> comp = new
         Comparator<Double>()
         {
            public int compare(Double d1, Double d2)
            {
               //update panel to show user current state of insertionsort
               panel.setValues(values, d1, d2);

               //put to sleep or die
               try
               {
                  Thread.sleep(delay);
               }
               catch (InterruptedException exception)
               {
                  Thread.currentThread().interrupt();
               }

               //return comparison and continue
               return (d1).compareTo(d2);
            }
         };
      Sorter.insertionSort(values, comp);
      panel.setValues(values, null, null);
   }

   private Double[] values;
   private ArrayComponent panel;
   private int delay;
}

/**
   This runnable executes a sort algorithm.
   When two elements are compared, the algorithm
   pauses and updates a panel.
*/
class SorterHeap implements Runnable
{
   /**
      Constructs the sorter.
      @param values the array to sort
      @param panel the panel for displaying the array
   */
   public SorterHeap(Double[] values, ArrayComponent panel, int delay)
   {
      this.values = values;
      this.panel = panel;
      this.delay = delay;
   }

   public void run()
   {
      Comparator<Double> comp = new
         Comparator<Double>()
         {
            public int compare(Double d1, Double d2)
            {
               //update panel to show user current state of heapsort
               panel.setValues(values, d1, d2);

               //put to sleep or die
               try
               {
                  Thread.sleep(delay);
               }
               catch (InterruptedException exception)
               {
                  Thread.currentThread().interrupt();
               }

               //return comparison and continue
               return (d1).compareTo(d2);
            }
         };
      Sorter.heapsort(values, comp);
      panel.setValues(values, null, null);
   }

   private Double[] values;
   private ArrayComponent panel;
   private static int delay;
}

/**
   This runnable executes a sort algorithm.
   When two elements are compared, the algorithm
   pauses and updates a panel.
*/
class SorterMerge implements Runnable
{
   /**
      Constructs the sorter.
      @param values the array to sort
      @param panel the panel for displaying the array
   */
   public SorterMerge(Double[] values, ArrayComponent panel, int delay)
   {
      this.values = values;
      this.panel = panel;
      this.delay = delay;
   }

   public void run()
   {
      Comparator<Double> comp = new
         Comparator<Double>()
         {
            public int compare(Double d1, Double d2)
            {
               //update panel to show user current state of mergesorter
               panel.setValues(values, d1, d2);

               //put to sleep or die
               try
               {
                  Thread.sleep(delay);
               }
               catch (InterruptedException exception)
               {
                  Thread.currentThread().interrupt();
               }

               //return comparison and continue
               return (d1).compareTo(d2);
            }
         };
      Sorter.mergeSort(values, comp);
      panel.setValues(values, null, null);
   }

   private Double[] values;
   private ArrayComponent panel;
   private int delay;
}

/**
   This runnable executes a sort algorithm.
   When two elements are compared, the algorithm
   pauses and updates a panel.
*/
class SorterQuick implements Runnable
{
   /**
      Constructs the sorter.
      @param values the array to sort
      @param panel the panel for displaying the array
   */
   public SorterQuick(Double[] values, ArrayComponent panel, int delay)
   {
      this.values = values;
      this.panel = panel;
      this.delay = delay;
   }

   public void run()
   {
      Comparator<Double> comp = new
         Comparator<Double>()
         {
            public int compare(Double d1, Double d2)
            {
               //update panel to show user current state of quicksort
               panel.setValues(values, d1, d2);

               //put to sleep or die
               try
               {
                  Thread.sleep(delay);
               }
               catch (InterruptedException exception)
               {
                  Thread.currentThread().interrupt();
               }

               //return comparison and continue
               return (d1).compareTo(d2);
            }
         };
      Sorter.quicksort(values, comp);
      panel.setValues(values, null, null);
   }

   private Double[] values;
   private ArrayComponent panel;
   private int delay;
}

