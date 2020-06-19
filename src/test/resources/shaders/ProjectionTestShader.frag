#version 430

in vec3 surfaceNormal;
in vec3 toLight;
in vec3 color;

//uniform vec3 color;

out vec4 out_Color;

void main(void){

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLight = normalize(toLight);

    float nDotl = dot(unitNormal, unitLight);
    float brightness = max(nDotl, 0);

    vec3 diffuse = brightness * vec3(1.0, 0, 0);

    out_Color = vec4(diffuse * color, 1.0);
}