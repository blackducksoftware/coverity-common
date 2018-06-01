package com.synopsys.integration.coverity

import com.synopsys.integration.coverity.CoverityVersion
import org.junit.Test

import static org.junit.Assert.*

class CoverityVersionTest {

    @Test
    public void testConstructors() {
        CoverityVersion coverityVersion = new CoverityVersion(9, 3)
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(0 == coverityVersion.patch)
        assertTrue(0 == coverityVersion.hotfix)
        assertFalse(coverityVersion.isSrmVersion)

        coverityVersion = new CoverityVersion(9, 3, 1, 2)
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(1 == coverityVersion.patch)
        assertTrue(2 == coverityVersion.hotfix)
        assertFalse(coverityVersion.isSrmVersion)
    }

    @Test
    public void testParse() {
        Optional<CoverityVersion> optionalCoverityVersion = CoverityVersion.parse(null)
        assertFalse(optionalCoverityVersion.isPresent())

        optionalCoverityVersion = CoverityVersion.parse("   ")
        assertFalse(optionalCoverityVersion.isPresent())

        optionalCoverityVersion = CoverityVersion.parse("stuff")
        assertFalse(optionalCoverityVersion.isPresent())

        optionalCoverityVersion = CoverityVersion.parse("9.3")
        assertTrue(optionalCoverityVersion.isPresent())
        CoverityVersion coverityVersion = optionalCoverityVersion.get()
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(0 == coverityVersion.patch)
        assertTrue(0 == coverityVersion.hotfix)
        assertTrue(coverityVersion.isSrmVersion)

        optionalCoverityVersion = CoverityVersion.parse("9.3-1")
        assertTrue(optionalCoverityVersion.isPresent())
        coverityVersion = optionalCoverityVersion.get()
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(1 == coverityVersion.patch)
        assertTrue(0 == coverityVersion.hotfix)
        assertTrue(coverityVersion.isSrmVersion)

        optionalCoverityVersion = CoverityVersion.parse("9.3-1-2")
        assertTrue(optionalCoverityVersion.isPresent())
        coverityVersion = optionalCoverityVersion.get()
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(1 == coverityVersion.patch)
        assertTrue(2 == coverityVersion.hotfix)
        assertTrue(coverityVersion.isSrmVersion)


        optionalCoverityVersion = CoverityVersion.parse("9.3.1")
        assertTrue(optionalCoverityVersion.isPresent())
        coverityVersion = optionalCoverityVersion.get()
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(1 == coverityVersion.patch)
        assertTrue(0 == coverityVersion.hotfix)
        assertFalse(coverityVersion.isSrmVersion)


        optionalCoverityVersion = CoverityVersion.parse("9.3.1.2")
        assertTrue(optionalCoverityVersion.isPresent())
        coverityVersion = optionalCoverityVersion.get()
        assertTrue(9 == coverityVersion.major)
        assertTrue(3 == coverityVersion.minor)
        assertTrue(1 == coverityVersion.patch)
        assertTrue(2 == coverityVersion.hotfix)
        assertFalse(coverityVersion.isSrmVersion)
    }

    @Test
    public void testToString() {
        CoverityVersion coverityVersion = new CoverityVersion(9, 3)
        assertEquals("9.3.0", coverityVersion.toString())

        coverityVersion = new CoverityVersion(9, 3, 1, 2)
        assertEquals("9.3.1.2", coverityVersion.toString())

        coverityVersion.isSrmVersion = true
        coverityVersion.srmVersion = "Stuff"
        assertEquals("Stuff", coverityVersion.toString())
    }

    @Test
    public void testCompareTo() {
        CoverityVersion coverityVersionOld = new CoverityVersion(9, 3)
        CoverityVersion coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareTo(coverityVersionNewer) < 0)
        assertTrue(coverityVersionNewer.compareTo(coverityVersionOld) > 0)

        coverityVersionOld = new CoverityVersion(8, 4, 1, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareTo(coverityVersionNewer) < 0)
        assertTrue(coverityVersionNewer.compareTo(coverityVersionOld) > 0)

        coverityVersionOld = new CoverityVersion(9, 4, 0, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareTo(coverityVersionNewer) < 0)
        assertTrue(coverityVersionNewer.compareTo(coverityVersionOld) > 0)



        coverityVersionOld = new CoverityVersion(9, 4, 1, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareTo(coverityVersionNewer) == 0)
        assertTrue(coverityVersionNewer.compareTo(coverityVersionOld) == 0)

        coverityVersionOld = new CoverityVersion(9, 4, 1, 1)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareTo(coverityVersionNewer) < 0)
        assertTrue(coverityVersionNewer.compareTo(coverityVersionOld) > 0)
    }

    @Test
    public void testCmp() {
        CoverityVersion coverityVersionOld = new CoverityVersion(0, 0)
        assertTrue(-1 == coverityVersionOld.cmp(0, 1))
        assertTrue(0 == coverityVersionOld.cmp(1, 1))
        assertTrue(1 == coverityVersionOld.cmp(2, 1))
    }

    @Test
    public void testCompareToAnalysis() {
        CoverityVersion coverityVersionOld = new CoverityVersion(9, 3)
        CoverityVersion coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertFalse(coverityVersionOld.compareToAnalysis(coverityVersionNewer))
        assertTrue(coverityVersionNewer.compareToAnalysis(coverityVersionOld))

        coverityVersionOld = new CoverityVersion(8, 4, 1, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertFalse(coverityVersionOld.compareToAnalysis(coverityVersionNewer))
        assertTrue(coverityVersionNewer.compareToAnalysis(coverityVersionOld))


        coverityVersionOld = new CoverityVersion(9, 4, 1, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareToAnalysis(coverityVersionNewer))
        assertTrue(coverityVersionNewer.compareToAnalysis(coverityVersionOld))

        coverityVersionOld = new CoverityVersion(9, 4, 1, 1)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.compareToAnalysis(coverityVersionNewer))
        assertTrue(coverityVersionNewer.compareToAnalysis(coverityVersionOld))
    }

    @Test
    public void testEquals() {
        CoverityVersion coverityVersionOld = new CoverityVersion(8, 4)
        assertTrue(coverityVersionOld.equals(coverityVersionOld))
        assertFalse(coverityVersionOld.equals(null))
        assertFalse(coverityVersionOld.equals(""))

        CoverityVersion coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertFalse(coverityVersionOld.equals(coverityVersionNewer))

        coverityVersionOld = new CoverityVersion(9, 3, 1, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertFalse(coverityVersionOld.equals(coverityVersionNewer))

        coverityVersionOld = new CoverityVersion(9, 4, 0, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertFalse(coverityVersionOld.equals(coverityVersionNewer))

        coverityVersionOld = new CoverityVersion(9, 4, 1, 0)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertFalse(coverityVersionOld.equals(coverityVersionNewer))

        coverityVersionOld = new CoverityVersion(9, 4, 1, 2)
        coverityVersionNewer = new CoverityVersion(9, 4, 1, 2)
        assertTrue(coverityVersionOld.equals(coverityVersionNewer))


    }

    @Test
    public void testHashCode() {
        CoverityVersion coverityVersion = new CoverityVersion(8, 4)
        int hashCode = coverityVersion.hashCode()
        assertTrue(String.valueOf(hashCode), 28871323 == hashCode)

        coverityVersion = new CoverityVersion(9, 4, 1, 2)
        hashCode = coverityVersion.hashCode()
        assertTrue(String.valueOf(hashCode), 28901147 == hashCode)
    }
}
