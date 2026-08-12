package de.team33.patterns.lazy.narvi.publics;

import de.team33.patterns.lazy.narvi.XLazy;

class XLazyTest extends LazyTestBase<XLazy<Integer, Exception>> {

    XLazyTest() {
        super(new Input<>(XLazy::init, lazy -> lazy));
    }
}
