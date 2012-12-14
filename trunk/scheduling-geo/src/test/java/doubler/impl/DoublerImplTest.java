package doubler.impl;

import org.testng.annotations.Test;

import doubler.Doubler;

public class DoublerImplTest {

	@Test
    public void testIt() {
        Doubler doubler = new DoublerImpl();
        assert doubler.doubleIt(2) == 4;
    }
}
