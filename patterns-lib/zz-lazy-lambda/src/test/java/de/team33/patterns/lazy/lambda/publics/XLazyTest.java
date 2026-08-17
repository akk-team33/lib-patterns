package de.team33.patterns.lazy.lambda.publics;

import de.team33.patterns.lazy.lambda.XLazy;

class XLazyTest extends LazyTestBase<XLazy<Integer, Exception>> {

    XLazyTest() {
        super(new Input<>(XLazy::init, lazy -> lazy));
    }
}
