#version 400

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightNormal;
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform sampler2D shadowMap;

const int pcfCount = 2;
const float totalTextels = (pcfCount * 2 + 1) * (pcfCount * 2 + 1) ;

void main(void){

        float mapSize = 4096;
        float textelSize = 1.0/mapSize;
        float total = 0;

        for (int x=-pcfCount; x<=pcfCount; x++){
            for(int y = -pcfCount; y<=pcfCount; y++){

                float objectClosestToLight = texture(shadowMap, shadowCoords.xy + vec2(x,y) * textelSize).r;

                if (shadowCoords.z > objectClosestToLight + 0.002){
                    total += 1;
                }
            }
        }

        total /= totalTextels;
        float lightFactor = 1.0 - ( total  * shadowCoords.w);

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLightNormal);
    vec3 unitToCameraVector = normalize(toCameraVector);

    float nDot1 = dot(unitToLight,unitNormal);
    float brightness = max(nDot1, 0.25);
    vec3 difuse = brightness * lightColor * lightFactor;
    vec3 lightDirection = -unitToLight;
    vec3 reflectedLghtDirection =reflect(lightDirection, unitNormal);
    float specularValue = max(dot(reflectedLghtDirection, unitToCameraVector), 0.0);
    float dampenedFactor = pow(specularValue, shineDamper);
    vec3 finalSpecular = dampenedFactor * lightColor;

	out_Color = vec4(difuse, 1.0)  * texture(textureSampler, pass_textureCoords) + vec4( finalSpecular, 0.0);

	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);

}