package transformer;

public interface Transformer<In, Out> {
    Out transform(In input);
}
