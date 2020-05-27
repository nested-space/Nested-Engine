#version 430

layout(location = 0) in vec3 position;

uniform float radiusPixels;
uniform mat4 modelMatrix;
uniform vec2 scale;

layout (std140) uniform WindowProperties
{
    vec2 size;
} window;

out vec2 innerCornerBL;
out vec2 innerCornerTR;
out vec2 innerCornerBR;
out vec2 innerCornerTL;
out vec2 cornerBL;
out vec2 cornerTR;
out vec2 cornerBR;
out vec2 cornerTL;

vec4 transform(vec3 coords, mat4 transMatrix){
    return transMatrix * vec4(coords, 1.0);
}

void main(void){
    gl_Position = transform(position, modelMatrix);

//    vec2 scale = vec2(0.5f, 0.5f);
    float glRadiusX = 2 * (1 / scale.x) * (radiusPixels / window.size.x);
    float glRadiusY = 2 * (1 / scale.y) * (radiusPixels / window.size.y);

    innerCornerBL = (transform(vec3(-1 + glRadiusX, -1 + glRadiusY, 0), modelMatrix).xy + 1) / 2 * window.size;
    innerCornerTR = (transform(vec3(1 - glRadiusX, 1 - glRadiusY, 0), modelMatrix).xy + 1) / 2 * window.size;
    innerCornerBR = (transform(vec3(1 - glRadiusX, -1 + glRadiusY, 0), modelMatrix).xy + 1) / 2 * window.size;
    innerCornerTL = (transform(vec3(-1 + glRadiusX, 1 - glRadiusY, 0), modelMatrix).xy + 1) / 2 * window.size;
    cornerBL = (transform(vec3(-1, -1, 0), modelMatrix).xy + 1) / 2 * window.size;
    cornerTR = (transform(vec3(1, 1, 0), modelMatrix).xy + 1) / 2 * window.size;
    cornerBR = (transform(vec3(1, -1, 0), modelMatrix).xy + 1) / 2 * window.size;
    cornerTL = (transform(vec3(-1, 1, 0), modelMatrix).xy + 1) / 2 * window.size;

}