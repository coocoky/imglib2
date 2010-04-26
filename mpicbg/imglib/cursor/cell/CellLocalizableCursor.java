/**
 * Copyright (c) 2009--2010, Stephan Preibisch & Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Stephan Preibisch & Stephan Saalfeld
 */
package mpicbg.imglib.cursor.cell;

import mpicbg.imglib.container.cell.CellContainer;
import mpicbg.imglib.cursor.LocalizableIterableCursor;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.Type;

public class CellLocalizableCursor<T extends Type<T>> extends CellIterableCursor<T> implements LocalizableIterableCursor<T>
{
	/* Inherited from CellCursor<T>
	protected final CellContainer<?,?> img;
	protected final int numCells;
	protected int cell;
	protected int lastCell;
	protected int cellMaxI;
	protected Cell<?,?> cellInstance;
	*/

	/*
	 * The number of dimensions of the image
	 */
	final protected int numDimensions;
	
	/*
	 * The position of the cursor inside the image
	 */
	final protected int[] position;
	
	/*
	 * The image dimensions
	 */
	final protected int[] dimensions;
	
	/*
	 * The dimension of the current cell
	 */
	final protected int[] cellDimensions;
	
	/*
	 * The offset of the current cell in the image
	 */
	final protected int[] cellOffset;
	
	public CellLocalizableCursor( final CellContainer<T,?> container, final Image<T> image, final T type )
	{
		super( container, image, type);

		numDimensions = container.numDimensions(); 
		
		position = new int[ numDimensions ];
		dimensions = container.getDimensions();
		
		cellDimensions = new int[ numDimensions ];
		cellOffset = new int[ numDimensions ];		
		
		// unluckily we have to call it twice, in the superclass position is not initialized yet
		reset();		
	}
	
	protected void getCellData( final int cell )
	{
		if ( cell == lastCell )
			return;
		
		lastCell = cell;		
		cellInstance = container.getCell( cell );		

		cellMaxI = cellInstance.getNumPixels();	
		cellInstance.getDimensions( cellDimensions );
		cellInstance.getOffset( cellOffset );
		
		type.updateContainer( this );
	}
	
	@Override
	public void reset()
	{
		if ( position != null )
		{
			type.updateIndex( -1 );
			cell = 0;
			getCellData( cell );
			isClosed = false;
			
			position[ 0 ] = -1;
			
			for ( int d = 1; d < numDimensions; d++ )
				position[ d ] = 0;
			
			type.updateContainer( this );
		}
		
		linkedIterator.reset();
	}
	
	@Override
	public void fwd()
	{
		if ( type.getIndex() < cellMaxI - 1 )
		{
			type.incIndex();
			
			for ( int d = 0; d < numDimensions; d++ )
			{
				if ( position[ d ] < cellDimensions[ d ] + cellOffset[ d ] - 1 )
				{
					position[ d ]++;
					
					for ( int e = 0; e < d; e++ )
						position[ e ] = cellOffset[ e ];
					
					break;
				}
			}
			
		}
		else if (cell < numCells - 1)
		{
			cell++;
			type.updateIndex( 0 );			
			getCellData(cell);
			for ( int d = 0; d < numDimensions; d++ )
				position[ d ] = cellOffset[ d ];
		}
		else
		{			
			// we have to run out of the image so that the next hasNext() fails
			lastCell = -1;						
			type.updateIndex( cellMaxI );
			cell = numCells;
		}
		
		linkedIterator.fwd();
	}	

	
	@Override
	public void localize( final float[] position )
	{
		for ( int d = 0; d < numDimensions; d++ )
			position[ d ] = this.position[ d ];
	}
	
	@Override
	public void localize( final double[] position )
	{
		for ( int d = 0; d < numDimensions; d++ )
			position[ d ] = this.position[ d ];
	}
	
	@Override
	public void localize( final int[] position )
	{
		for ( int d = 0; d < numDimensions; d++ )
			position[ d ] = ( int )this.position[ d ];
	}
	
	@Override
	public void localize( final long[] position )
	{
		for ( int d = 0; d < numDimensions; d++ )
			position[ d ] = this.position[ d ];
	}
	

	@Override
	public float getFloatPosition( final int d ){ return position[ d ]; }
	
	@Override
	public double getDoublePosition( final int d ){ return position[ d ]; }
	
	@Override
	public int getIntPosition( final int dim ){ return ( int )position[ dim ]; }	
	
	@Override
	public long getLongPosition( final int dim ){ return position[ dim ]; }	
	
	@Override
	public String getLocationAsString()
	{
		String pos = "(" + position[ 0 ];
		
		for ( int d = 1; d < numDimensions; d++ )
			pos += ", " + position[ d ];
		
		pos += ")";
		
		return pos;
	}
	
	@Override
	public String toString() { return getLocationAsString() + " = " + type(); }
	
	@Override
	public int numDimensions(){ return numDimensions; }
}
