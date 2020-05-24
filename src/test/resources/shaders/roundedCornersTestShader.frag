#version 430

in vec2 cornerBL;
in vec2 cornerTR;

uniform float radiusPixels;

out vec4 out_Color;

vec4 cornerTest(vec2 pixelPosition, vec2 cornerPixelPosition){
    if (length(pixelPosition - cornerPixelPosition) > radiusPixels){
        return vec4(1.0, 0.0, 0.0, 1.0);
    } else {
        return vec4(0.0, 0.0, 0.0, 1.0);
    }
}

void main(void) {

    out_Color = vec4(0.0, 1.0, 0.0, 1.0);

    vec2 cornerBR = vec2(cornerTR.x, cornerBL.y);
    vec2 cornerTL = vec2(cornerBL.x, cornerTR.y);

    if (gl_FragCoord.x < cornerBL.x && gl_FragCoord.y < cornerBL.y){
        out_Color =cornerTest(gl_FragCoord.xy, cornerBL);
    }

    if (gl_FragCoord.x > cornerBR.x && gl_FragCoord.y < cornerBR.y){
        out_Color =cornerTest(gl_FragCoord.xy, cornerBR);
    }

    if (gl_FragCoord.x < cornerTL.x && gl_FragCoord.y > cornerTL.y){
        out_Color =cornerTest(gl_FragCoord.xy, cornerTL);
    }

    if (gl_FragCoord.x > cornerTR.x && gl_FragCoord.y > cornerTR.y){
        out_Color =cornerTest(gl_FragCoord.xy, cornerTR);
    }


}