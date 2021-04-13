package de.team33.patterns.exceptional.v1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Objects;

public class CasesExample {

    public void sample1() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            throw Cases.of(caught.getCause())
                       .reThrow(IOException.class)
                       .reThrow(SQLException.class)
                       .reThrow(URISyntaxException.class)
                       .throwIf(Objects::isNull,
                                cause -> new ExpectationException("WrappedException without cause!?", caught))
                       // Technically, it could happen that our expectations are not met.
                       // To be on the safe side, this should lead to a meaningful exception ...
                       .quit(ExpectationException::new);
        }
    }

    public void sample2() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            final Throwable cause = Cases.of(caught.getCause())
                                         .reThrow(IOException.class)
                                         .reThrow(SQLException.class)
                                         .reThrow(URISyntaxException.class)
                                         .quit();
            // Technically, it could happen that our expectations are not met.
            // To be on the safe side, this should lead to a meaningful exception ...
            throw new ExpectationException(cause);
        }
    }

    private void doSomethingThatMayThrowAWrappedException() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
