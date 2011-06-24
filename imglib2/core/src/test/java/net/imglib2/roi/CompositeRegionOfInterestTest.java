/**
 * 
 */
package net.imglib2.roi;

import static org.junit.Assert.*;

import java.util.ArrayList;

import net.imglib2.RealRandomAccess;
import net.imglib2.type.logic.BitType;

import org.junit.Test;

/**
 * @author Lee Kamentsky
 *
 */
public class CompositeRegionOfInterestTest {

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#CompositeRegionOfInterest(int)}.
	 */
	@Test
	public void testCompositeRegionOfInterestInt() {
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(2);
		assertEquals(2, c.numDimensions());
	}

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#CompositeRegionOfInterest(net.imglib2.roi.RegionOfInterest)}.
	 */
	@Test
	public void testCompositeRegionOfInterestRegionOfInterest() {
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}));
		assertEquals(2, c.numDimensions());
	}

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#CompositeRegionOfInterest(java.util.Collection)}.
	 */
	@Test
	public void testCompositeRegionOfInterestCollectionOfRegionOfInterest() {
		ArrayList<RegionOfInterest> list = new ArrayList<RegionOfInterest>();
		list.add(new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}));
		list.add(new RectangleRegionOfInterest(new double[] {5,6}, new double[] { 3,4}));
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(list);
		assertEquals(2, c.numDimensions());
	}

	private void assertInside(CompositeRegionOfInterest c, double [] position) {
		RealRandomAccess<BitType> ra = c.realRandomAccess();
		ra.setPosition(position);
		assertTrue(ra.get().get());
	}
	private void assertInside(CompositeRegionOfInterest c, double x, double y) {
		assertInside(c,new double[] { x,y});
	}

	private void assertOutside(CompositeRegionOfInterest c, double [] position) {
		RealRandomAccess<BitType> ra = c.realRandomAccess();
		ra.setPosition(position);
		assertFalse(ra.get().get());
	}
	private void assertOutside(CompositeRegionOfInterest c, double x, double y) {
		assertOutside(c,new double[] { x,y});
	}
	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#or(net.imglib2.roi.RegionOfInterest)}.
	 */
	@Test
	public void testOr() {
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}));
		c.or(new RectangleRegionOfInterest(new double[] {3,5}, new double[] { 3,4}));
		assertInside(c, 2,3);
		assertInside(c, 5, 8);
		assertInside(c, 3.5, 5.5);
		assertOutside(c, 0, 0);
	}

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#remove(net.imglib2.roi.RegionOfInterest)}.
	 */
	@Test
	public void testRemove() {
		RectangleRegionOfInterest [] rois = new RectangleRegionOfInterest[] {
			new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}),
			new RectangleRegionOfInterest(new double[] {3,5}, new double[] { 3,4})
		};
		double [][] inside = { {2,4},{5,8}};
		for (int i=0; i<2; i++) {
			CompositeRegionOfInterest c = new CompositeRegionOfInterest(rois[0]);
			c.or(rois[1]);
			c.remove(rois[i]);
			for (int j=0; j<2; j++) {
				if (i == j) {
					assertOutside(c, inside[j]);
				} else {
					assertInside(c, inside[j]);
				}
			}
		}
	}

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#and(net.imglib2.roi.RegionOfInterest)}.
	 */
	@Test
	public void testAnd() {
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}));
		c.and(new RectangleRegionOfInterest(new double[] {3,5}, new double[] { 3,4}));
		assertOutside(c, 2,3);
		assertOutside(c, 5, 8);
		assertInside(c, 3.5, 5.5);
		assertOutside(c, 0, 0);
	}

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#xor(net.imglib2.roi.RegionOfInterest)}.
	 */
	@Test
	public void testXor() {
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}));
		c.xor(new RectangleRegionOfInterest(new double[] {3,5}, new double[] { 3,4}));
		assertInside(c, 2,3);
		assertInside(c, 5, 8);
		assertOutside(c, 3.5, 5.5);
		assertOutside(c, 0, 0);
	}

	/**
	 * Test method for {@link net.imglib2.roi.CompositeRegionOfInterest#not(net.imglib2.roi.RegionOfInterest)}.
	 */
	@Test
	public void testNot() {
		CompositeRegionOfInterest c = new CompositeRegionOfInterest(new RectangleRegionOfInterest(new double[] {1,2}, new double[] { 3,4}));
		c.not(new RectangleRegionOfInterest(new double[] {3,5}, new double[] { 3,4}));
		assertInside(c, 2,3);
		assertOutside(c, 5, 8);
		assertOutside(c, 3.5, 5.5);
		assertOutside(c, 0, 0);
	}

}