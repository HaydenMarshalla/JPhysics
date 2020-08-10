package library;

public class Colour {
    Colour(float rIn, float gIn, float bIn, float aIn) {
        set( rIn,  gIn,  bIn, aIn);
    }

    void set(float rIn, float gIn, float bIn, float aIn)
    {
        r = rIn; g = gIn; b = bIn; a = aIn;
    }

    float r, g, b, a;
}