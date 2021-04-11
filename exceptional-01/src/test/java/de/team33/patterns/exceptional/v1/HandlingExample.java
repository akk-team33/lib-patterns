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
                          .reThrowAs(IOException.class)
                          .reThrowAs(SQLException.class)
                          .reThrowAs(URISyntaxException.class)
                          .when(Objects::isNull)
                          .thenThrow(cause -> new ExpectationException("WrappedException without cause!?", caught))
                          // Technically, it could happen that our expectations are not met.
                          // To be on the safe side, this should lead to a meaningful exception ...
                          .map(ExpectationException::new);
        }
    }

    public void alt() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            final Throwable cause = Handling.of(caught.getCause())
                                            .reThrowAs(IOException.class)
                                            .reThrowAs(SQLException.class)
                                            .reThrowAs(URISyntaxException.class)
                                            .get();
            // Technically, it could happen that our expectations are not met.
            // To be on the safe side, this should lead to a meaningful exception ...
            throw new ExpectationException(cause);
        }
    }

    private void doSomethingThatMayThrowAWrappedException() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
