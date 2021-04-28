package de.team33.patterns.exceptional.v1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;


public class ReviewExample<R> {

    public final R example1() throws IOException, SQLException, URISyntaxException {
        try {
            return somethingThatMayCauseAWrappedException();
        } catch (final WrappedException caught) {
            throw Review.of(caught.getCause())
                        .reThrow(IOException.class)
                        .reThrow(SQLException.class)
                        .reThrow(URISyntaxException.class)
                        .finish(ExpectationException::new);
        }
    }

    private R somethingThatMayCauseAWrappedException() {
        throw new WrappedException(new IOException("not yet implemented"));
    }
}
