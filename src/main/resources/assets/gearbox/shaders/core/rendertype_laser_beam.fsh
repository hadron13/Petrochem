#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
//uniform float FogStart;
//uniform float FogEnd;
//uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord1;
in vec3 normal;
in vec3 position;

out vec4 fragColor;

void main() {
    float sine = (sin(texCoord0.y * 8.0 + GameTime * 30000.0f)+1.0f)/2.0f;
    //fragColor = vec4((sine/2) + 0.5 * vertexColor.xyz /*- step(99.0f, texCoord0.y) *  (100.0f - texCoord0.y)*/, 0.8);
    fragColor = vec4(vertexColor.xyz, 0.8);
}
