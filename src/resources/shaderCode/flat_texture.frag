#version 430

in vec2 uv_coordinates;
uniform sampler2D textureSampler;

out vec4 out_Color;

void main(void){
    out_Color = texture(textureSampler, uv_coordinates);
}