#version 430

uniform vec3 color;

out vec4 out_Color;

void main(void){
    out_Color = vec4(color, 1.0);
}