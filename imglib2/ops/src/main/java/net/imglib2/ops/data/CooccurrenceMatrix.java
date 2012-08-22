package net.imglib2.ops.data;

import java.util.Arrays;

import net.imglib2.ops.operation.iterableinterval.unary.MakeCooccurrenceMatrix;
import net.imglib2.ops.operation.iterableinterval.unary.MakeCooccurrenceMatrix.HaralickFeature;

/**
 * This Helper Class holds a co-occurrence matrix. It should be generated by
 * {@link MakeCooccurrenceMatrix}. It's features are ordered according to
 * {@link HaralickFeature}
 * 
 * @author Stephan Sellien, University of Konstanz
 */
public class CooccurrenceMatrix
{

	public static enum MatrixOrientation
	{
		DIAGONAL( 1, -1 ), ANTIDIAGONAL( 1, 1 ), HORIZONTAL( 1, 0 ), VERTICAL( 0, 1 );

		public final int dx;

		public final int dy;

		private MatrixOrientation( int dx, int dy )
		{
			this.dx = dx;
			this.dy = dy;
		}
	}

	// actual matrix
	private double[][] m_matrix = null;

	// feature vector
	private double[] m_haralickFeatures;

	/**
	 * Constructor creates co-occurrence matrix with given size (e.g. number of
	 * gray levels).
	 * 
	 * @param size
	 */
	public CooccurrenceMatrix( int size )
	{
		m_matrix = new double[ size ][ size ];
	}

	/**
	 * Returns the value at row, col of the co-occurrence matrix.
	 * 
	 * @param row
	 *            - the row
	 * @param col
	 *            - the column
	 * @return value at that position
	 */
	public final double getValueAt( final int row, final int col )
	{
		return m_matrix[ row ][ col ];
	}

	/**
	 * Increases matrix cell by one.
	 * 
	 * @param row
	 *            row
	 * @param col
	 *            col
	 */
	public final void incValueAt( final int row, final int col )
	{
		m_matrix[ row ][ col ]++;
	}

	/**
	 * Divides each entry of the matrix by divisor.
	 * 
	 * @param divisor
	 *            the divisor
	 */
	public final void divideBy( int divisor )
	{
		for ( int row = 0; row < m_matrix.length; row++ )
		{
			for ( int col = 0; col < m_matrix[ row ].length; col++ )
			{
				m_matrix[ row ][ col ] /= divisor;
			}
		}
	}

	/**
	 * Clears the matrix by filling with zeros.
	 */
	public void clear()
	{
		for ( int row = 0; row < m_matrix.length; row++ )
		{
			Arrays.fill( m_matrix[ row ], 0 );
		}
	}

	/**
	 * Sets the feature array. Must be according to {@link HaralickFeature}.
	 * 
	 * @param h
	 *            the feature vector
	 */
	public void setFeatures( double[] h )
	{
		m_haralickFeatures = h;
	}

	/**
	 * Returns the feature with given index. Returns NaN if no features are set
	 * or on invalid index.
	 * 
	 * @param id
	 *            the feature index
	 * @return the value
	 */
	public double getFeature( int id )
	{
		if ( m_haralickFeatures == null || id < 0 || id >= m_haralickFeatures.length )
			return Double.NaN;
		return m_haralickFeatures[ id ];
	}

}
