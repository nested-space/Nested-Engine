#version 430

in vec3 surfaceNormal;
in vec3 toLight;

uniform vec3 color;

out vec4 out_Color;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLight = normalize(toLight);

    float nDotl = dot(unitNormal, unitLight);
    float brightness = max(nDotl, 0.1);

    vec3 diffuse = brightness * color;

    out_Color = vec4(diffuse, 1.0);
}