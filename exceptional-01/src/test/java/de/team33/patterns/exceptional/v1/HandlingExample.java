package de.team33.patterns.exceptional.v1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Objects;

public class HandlingExample {

    public void run() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            throw Handling.of(caught.getCause())
                          .reThrowCauseIf(IOException.class)
                          .reThrowCauseIf(SQLException.class)
                          .reThrowCauseIf(URISyntaxException.class)
                          // Technically, it could happen that our expectations are not met.
                          // To be on the safe side, this should lead to a meaningful exception ...
                          .mapped(ExpectationException::new);
        }
    }

    public void alt() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            final Throwable cause = Handling.of(caught.getCause())
                                            .reThrowCauseIf(IOException.class)
                                            .reThrowCauseIf(SQLException.class)
                                            .reThrowCauseIf(URISyntaxException.class)
                                            .fallback();
            // Technically, it could happen that our expectations are not met.
            // To be on the safe side, this should lead to a meaningful exception ...
            throw new ExpectationException(cause);
        }
    }

    private void doSomethingThatMayThrowAWrappedException() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
