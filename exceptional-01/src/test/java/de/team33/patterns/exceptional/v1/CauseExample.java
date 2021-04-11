package de.team33.patterns.exceptional.v1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class CauseExample {

    public void run() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            throw Cause.of(caught)
                       .reThrowIf(IOException.class)
                       .reThrowIf(SQLException.class)
                       .reThrowIf(URISyntaxException.class)
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
            final Throwable cause = Cause.of(caught)
                                             .reThrowIf(IOException.class)
                                             .reThrowIf(SQLException.class)
                                             .reThrowIf(URISyntaxException.class)
                                             .reGet();
            // Technically, it could happen that our expectations are not met.
            // To be on the safe side, this should lead to a meaningful exception ...
            throw new ExpectationException(cause);
        }
    }

    private void doSomethingThatMayThrowAWrappedException() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
