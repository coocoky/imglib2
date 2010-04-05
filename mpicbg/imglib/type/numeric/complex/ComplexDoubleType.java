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
package mpicbg.imglib.type.numeric.complex;

import mpicbg.imglib.container.DirectAccessContainer;
import mpicbg.imglib.container.DirectAccessContainerFactory;
import mpicbg.imglib.container.basictypecontainer.DoubleAccess;
import mpicbg.imglib.container.basictypecontainer.FloatAccess;
import mpicbg.imglib.container.basictypecontainer.array.DoubleArray;
import mpicbg.imglib.cursor.Cursor;
import mpicbg.imglib.type.numeric.ComplexType;

public class ComplexDoubleType extends ComplexTypeImpl<ComplexDoubleType> implements ComplexType<ComplexDoubleType>
{
	// the DirectAccessContainer
	final DirectAccessContainer<ComplexDoubleType, DoubleAccess> storage;
	
	// the (sub)DirectAccessContainer that holds the information 
	DoubleAccess b;
	
	// this is the constructor if you want it to read from an array
	public ComplexDoubleType( DirectAccessContainer<ComplexDoubleType, DoubleAccess> complexfloatStorage )
	{
		storage = complexfloatStorage;
		realI = 0;
		complexI = 1;
	}
	
	// this is the constructor if you want it to be a variable
	public ComplexDoubleType( final float real, final float complex )
	{
		storage = null;
		b = new DoubleArray( 2 );
		set( real, complex );
		realI = 0;
		complexI = 1;
	}

	// this is the constructor if you want it to be a variable
	public ComplexDoubleType() { this( 0, 0 ); }

	@Override
	public DirectAccessContainer<ComplexDoubleType, ? extends FloatAccess> createSuitableDirectAccessContainer( final DirectAccessContainerFactory storageFactory, final int dim[] )
	{
		return storageFactory.createFloatInstance( dim, 2 );	
	}

	@Override
	public void updateContainer( final Cursor<?> c ) 
	{ 
		b = storage.update( c );		
	}
	
	@Override
	public float getRealFloat() { return (float)b.getValue( realI ); }
	@Override
	public double getRealDouble() { return b.getValue( realI ); }
	@Override
	public float getComplexFloat() { return (float)b.getValue( complexI ); }
	@Override
	public double getComplexDouble() { return b.getValue( complexI ); }
	
	@Override
	public void setReal( final float real ){ b.setValue( realI, real ); }
	@Override
	public void setReal( final double real ){ b.setValue( realI, real ); }
	@Override
	public void setComplex( final float complex ){ b.setValue( complexI, complex ); }
	@Override
	public void setComplex( final double complex ){ b.setValue( complexI, complex ); }
	
	public void set( final float real, final float complex ) 
	{ 
		b.setValue( realI, real );
		b.setValue( complexI, complex );
	}

	@Override
	public void set( final ComplexDoubleType c ) 
	{ 
		setReal( c.getRealDouble() );
		setComplex( c.getComplexDouble() );
	}

	@Override
	public ComplexDoubleType[] createArray1D(int size1){ return new ComplexDoubleType[ size1 ]; }

	@Override
	public ComplexDoubleType[][] createArray2D(int size1, int size2){ return new ComplexDoubleType[ size1 ][ size2 ]; }

	@Override
	public ComplexDoubleType[][][] createArray3D(int size1, int size2, int size3) { return new ComplexDoubleType[ size1 ][ size2 ][ size3 ]; }

	//@Override
	//public ComplexFloatType getType() { return this; }

	@Override
	public ComplexDoubleType createDirectAccessType( DirectAccessContainer<ComplexDoubleType,?> DirectAccessContainer )
	{
		return new ComplexDoubleType( (DirectAccessContainer<ComplexDoubleType, DoubleAccess>)DirectAccessContainer );
	}
	
	@Override
	public ComplexDoubleType createVariable(){ return new ComplexDoubleType( 0, 0 ); }
	
	@Override
	public ComplexDoubleType clone(){ return new ComplexDoubleType( getRealFloat(), getComplexFloat() ); }	
}
