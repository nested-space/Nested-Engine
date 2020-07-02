#version 430

in vec2 cornerBL;
in vec2 cornerTR;
in vec2 cornerBR;
in vec2 cornerTL;
in vec2 innerCornerBL;
in vec2 innerCornerTR;
in vec2 innerCornerBR;
in vec2 innerCornerTL;

uniform float radiusPixels;

out vec4 out_Color;

float lengthSquared(vec2 a, vec2 b){
    return (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
}

double area(vec2 point1, vec2 point2, vec2 point3) {
    return abs((point1.x*(point2.y-point3.y) + point2.x*(point3.y-point1.y)+point3.x*(point1.y-point2.y))/2.0);
}

bool innPaddedCornerArea(){
    float sqRadius = radiusPixels * radiusPixels;
    if(lengthSquared(gl_FragCoord.xy, cornerBL) <= sqRadius) {
        return lengthSquared(gl_FragCoord.xy, innerCornerBL) >= sqRadius;
    }
    if(lengthSquared(gl_FragCoord.xy, cornerBR)<= sqRadius){
        return lengthSquared(gl_FragCoord.xy, innerCornerBR) >= sqRadius;
    }
    if(lengthSquared(gl_FragCoord.xy, cornerTL)<= sqRadius) {
        return lengthSquared(gl_FragCoord.xy, innerCornerTL) >= sqRadius;
    }
    if(lengthSquared(gl_FragCoord.xy, cornerTR)<= sqRadius) {
        return lengthSquared(gl_FragCoord.xy, innerCornerTR) >= sqRadius;
    }
    return false;
}

void main(void) {
    if (innPaddedCornerArea()){
        discard;
    } else {
        out_Color = vec4(0.0, 0.0, 1.0, 1.0);
    }
}