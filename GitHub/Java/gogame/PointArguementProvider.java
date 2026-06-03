package gogame;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class PointArguementProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context){
        return Stream.of(
                Arguments.of(new Point(3,2),new Point[]{new Point(2, 2), new Point(4, 2),new Point(3, 1), new Point(3, 3)}),
                Arguments.of(new Point(0, 0), new Point[]{new Point(0, 1), new Point(1, 0)}),
                Arguments.of(new Point(6, 8), new Point[]{ new Point(5, 8), new Point(4, 8), new Point(6, 7)})
        );
    }

}
