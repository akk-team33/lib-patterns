package de.team33.patterns.exceptional.dione;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;


public class ReviewExample<R> {

    public final R example1() throws IOException, SQLException, URISyntaxException {
        try {
            return somethingThatMayCauseAWrappedException();
        } catch (final WrappedException caught) {
            throw Revision.of(caught.getCause())
                          .reThrow(IOException.class)
                          .reThrow(SQLException.class)
                          .reThrow(URISyntaxException.class)
                          .close(ExpectationException::new);
        }
    }

    private R somethingThatMayCauseAWrappedException() {
        throw new WrappedException(new IOException("not yet implemented"));
    }
}
