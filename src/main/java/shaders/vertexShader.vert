#version 400

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightNormal;
out vec3 toCameraVector;
out float visibility;
out vec4 shadowCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform mat4 toShadowMapSpace;

const float density = 0.0015;
const float gradient = 5;

void main(void){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    shadowCoords = toShadowMapSpace * worldPosition;

	gl_Position = projectionMatrix * positionRelativeToCam;
    pass_textureCoords = textureCoords;

    surfaceNormal = (transformationMatrix * vec4(normal.xyz, 0.0)).xyz;

    toLightNormal = lightPosition - worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * density),gradient));

    visibility = clamp(visibility, 0.0, 1.0);

}