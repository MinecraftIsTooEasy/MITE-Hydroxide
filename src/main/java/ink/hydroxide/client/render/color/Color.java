package ink.hydroxide.client.render.color;


import ink.hydroxide.client.misc.ICopyable;
import ink.hydroxide.client.misc.ISerializable;
import net.minecraft.EnumChatFormatting;
import net.minecraft.NBTTagCompound;

@SuppressWarnings("unused")
public class Color implements ICopyable<Color>, ISerializable<Color> {

    public static final Color WHITE = new Color(java.awt.Color.WHITE);
    public static final Color LIGHT_GRAY = new Color(java.awt.Color.LIGHT_GRAY);
    public static final Color GRAY = new Color(java.awt.Color.GRAY);
    public static final Color DARK_GRAY = new Color(java.awt.Color.DARK_GRAY);
    public static final Color BLACK = new Color(java.awt.Color.BLACK);
    public static final Color RED = new Color(java.awt.Color.RED);
    public static final Color PINK = new Color(java.awt.Color.PINK);
    public static final Color ORANGE = new Color(java.awt.Color.ORANGE);
    public static final Color YELLOW = new Color(java.awt.Color.YELLOW);
    public static final Color GREEN = new Color(java.awt.Color.GREEN);
    public static final Color MAGENTA = new Color(java.awt.Color.MAGENTA);
    public static final Color CYAN = new Color(java.awt.Color.CYAN);
    public static final Color BLUE = new Color(java.awt.Color.BLUE);

    public int r, g, b, a;

    public Color() {
        this(255, 255, 255, 255);
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;

        validate();
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        validate();
    }

    public Color(float r, float g, float b, float a) {
        this.r = (int)(r*255);
        this.g = (int)(g*255);
        this.b = (int)(b*255);
        this.a = (int)(a*255);

        validate();
    }

    public Color(int packed) {
        this.r = toRGBAR(packed);
        this.g = toRGBAG(packed);
        this.b = toRGBAB(packed);
        this.a = toRGBAA(packed);
    }

    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    public Color(java.awt.Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
        this.a = color.getAlpha();
    }

    public Color(EnumChatFormatting formatting) {
        if (formatting.isColor()) {
            this.r = toRGBAR(formatting.rgb);
            this.g = toRGBAG(formatting.rgb);
            this.b = toRGBAB(formatting.rgb);
            this.a = toRGBAA(formatting.rgb);
        } else {
            this.r = 255;
            this.g = 255;
            this.b = 255;
            this.a = 255;
        }
    }


    public static int fromRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b) + (a << 24);
    }

    public static int toRGBAR(int color) {
        return (color >> 16) & 0x000000FF;
    }

    public static int toRGBAG(int color) {
        return (color >> 8) & 0x000000FF;
    }

    public static int toRGBAB(int color) {
        return (color) & 0x000000FF;
    }

    public static int toRGBAA(int color) {
        return (color >> 24) & 0x000000FF;
    }

    public static Color fromHsv(double h, double s, double v) {
        double hh, p, q, t, ff;
        int i;
        double r, g, b;

        if (s <= 0.0) {       // < is bogus, just shuts up warnings
            r = v;
            g = v;
            b = v;
            return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255), 255);
        }
        hh = h;
        if (hh >= 360.0) hh = 0.0;
        hh /= 60.0;
        i = (int) hh;
        ff = hh - i;
        p = v * (1.0 - s);
        q = v * (1.0 - (s * ff));
        t = v * (1.0 - (s * (1.0 - ff)));

        switch (i) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;

            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
            default:
                r = v;
                g = p;
                b = q;
                break;
        }
        return new Color((int) (r * 255), (int) (g * 255), (int) (b * 255), 255);
    }

    public Color set(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        validate();

        return this;
    }

    public Color r(int r) {
        this.r = r;
        validate();
        return this;
    }

    public Color g(int g) {
        this.g = g;
        validate();
        return this;
    }

    public Color b(int b) {
        this.b = b;
        validate();
        return this;
    }

    public Color a(int a) {
        this.a = a;
        validate();
        return this;
    }

    @Override
    public Color set(Color value) {
        r = value.r;
        g = value.g;
        b = value.b;
        a = value.a;

        validate();

        return this;
    }

    public boolean parse(String text) {
        String[] split = text.split(",");
        if (split.length != 3 && split.length != 4) return false;

        try {
            // Not assigned directly because of exception handling
            int r = Integer.parseInt(split[0]);
            int g = Integer.parseInt(split[1]);
            int b = Integer.parseInt(split[2]);
            int a = split.length == 4 ? Integer.parseInt(split[3]) : this.a;

            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;

            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Override
    public Color copy() {
        return new Color(r, g, b, a);
    }

    public void validate() {
        if (r < 0) r = 0;
        else if (r > 255) r = 255;

        if (g < 0) g = 0;
        else if (g > 255) g = 255;

        if (b < 0) b = 0;
        else if (b > 255) b = 255;

        if (a < 0) a = 0;
        else if (a > 255) a = 255;
    }


    public int getPacked() {
        return fromRGBA(r, g, b, a);
    }

    @Override
    public NBTTagCompound toTag() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("r", r);
        tag.setInteger("g", g);
        tag.setInteger("b", b);
        tag.setInteger("a", a);

        return tag;
    }

    @Override
    public Color fromTag(NBTTagCompound tag) {
        r = tag.getInteger("r");
        g = tag.getInteger("g");
        b = tag.getInteger("b");
        a = tag.getInteger("a");

        validate();
        return this;
    }

    @Override
    public String toString() {
        return r + " " + g + " " + b + " " + a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        return r == color.r && g == color.g && b == color.b && a == color.a;
    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + g;
        result = 31 * result + b;
        result = 31 * result + a;
        return result;
    }
}