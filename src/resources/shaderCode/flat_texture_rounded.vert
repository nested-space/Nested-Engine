#version 430

layout(location = 0) in vec3 position;
layout(location = 2) in vec2 textureCoordinates;

out vec2 uv_coordinates;

void main(void){

//    vec2 coords = a_uv * u_dimensions;
//    if (length(coords - vec2(0) < u_radius ||
//    length(coords - vec2(0, u_dimensions.y) < u_radius ||
//    length(coords - vec2(u_dimensions.x, 0) < u_radius ||
//    length(coords - u_dimensions) < u_radius) {
//        discard;
//    }
//    // Do everything else otherwise

    gl_Position = vec4(position,1.0);
    uv_coordinates = textureCoordinates;
}