#version 430

layout(location = 0) in vec2 position;

uniform float radiusPixels;
uniform mat4 transformationMatrix;

layout (std140) uniform WindowProperties
{
    vec2 size;
} window;

out vec2 cornerBL;
out vec2 cornerTR;

vec4 transform(vec2 coords, mat4 transMatrix){
    return transMatrix * vec4(coords, 1.0, 1.0);
}

void main(void){

    gl_Position = transform(position, transformationMatrix);

    vec2 glMin = transform(vec2(-1, -1), transformationMatrix).xy;
    vec2 glMax = transform(vec2(1, 1), transformationMatrix).xy;

    cornerBL = ((glMin + 1)/2 * window.size) + radiusPixels;
    cornerTR = ((glMax + 1)/2 * window.size) - radiusPixels;
}