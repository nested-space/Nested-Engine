#version 430

layout(location = 0) in vec2 position;

layout (std140) uniform TestBlock
{
	mat4 padding;
	vec3 color;
} ;

out vec4 vec_out_color;

void main(void){

	gl_Position = vec4(position, 0.0 ,1.0);
	vec_out_color = vec4(color, 0.1);
}