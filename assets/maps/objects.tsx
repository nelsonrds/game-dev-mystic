<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="object" tilewidth="80" tileheight="112" tilecount="5" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="objects/house.png" width="80" height="112"/>
 </tile>
 <tile id="1" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="objects/oak_tree.png" width="41" height="63"/>
 </tile>
 <tile id="2">
  <properties>
   <property name="animation" value="IDLE"/>
   <property name="animationSpeed" type="float" value="1"/>
   <property name="atlasAsset" value="OBJECTS"/>
   <property name="controller" type="bool" value="true"/>
   <property name="speed" type="float" value="4"/>
  </properties>
  <image source="objects/player.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10.1875" y="17.625" width="10.8125" height="5.125">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="3" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="../../assets_raw/objects/chest/chest.png" width="16" height="16"/>
 </tile>
 <tile id="6" type="Prop">
  <properties>
   <property name="atlasAsset" value="OBJECTS"/>
  </properties>
  <image source="../../assets_raw/objects/form/Sprite-0001.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="2.34783" y="7.17391">
    <polygon points="0,0 11,0.0434783 11.2174,7.30435 0.173913,7.26087"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
