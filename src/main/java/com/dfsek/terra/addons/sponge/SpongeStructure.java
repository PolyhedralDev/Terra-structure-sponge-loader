/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.sponge;

import net.jafama.FastMath;

import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.buffer.items.BufferedBlock;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;


public class SpongeStructure implements Structure {
    
    private final BlockState[][][] blocks;
    private final Platform platform;
    
    private final String id;
    
    public SpongeStructure(BlockState[][][] blocks, Platform platform, String id) {
        this.blocks = blocks;
        this.platform = platform;
        this.id = id;
    }
    
    @Override
    public boolean generate(Vector3 location, World world, Chunk chunk, Random random, Rotation rotation) {
        int bX = location.getBlockX();
        int bY = location.getBlockY();
        int bZ = location.getBlockZ();
        for(int x = 0; x < blocks.length; x++) {
            for(int z = 0; z < blocks[x].length; z++) {
                Vector2 r = RotationUtil.rotateVector(new Vector2(x, z), rotation);
                int rX = r.getBlockX();
                int rZ = r.getBlockZ();
                if(FastMath.floorDiv(bX + rX, 16) != chunk.getX() || FastMath.floorDiv(bZ + rZ, 16) != chunk.getZ()) {
                    continue;
                }
                for(int y = 0; y < blocks[z].length; y++) {
                    BlockState state = blocks[x][z][y];
                    if(state == null) continue;
                    world.setBlockData(bX + rX, bY + y, bZ + rZ, state);
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions) {
        for(int x = 0; x < blocks.length; x++) {
            for(int z = 0; z < blocks[x].length; z++) {
                Vector2 r = RotationUtil.rotateVector(new Vector2(x, z), rotation);
                int rX = r.getBlockX();
                int rZ = r.getBlockZ();
                for(int y = 0; y < blocks[z].length; y++) {
                    BlockState state = blocks[x][z][y];
                    if(state == null) continue;
                    buffer.addItem(new BufferedBlock(state, true, platform, false), new Vector3(rX, y, rZ));
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean generate(Vector3 location, World world, Random random, Rotation rotation) {
        int bX = location.getBlockX();
        int bY = location.getBlockY();
        int bZ = location.getBlockZ();
        for(int x = 0; x < blocks.length; x++) {
            for(int z = 0; z < blocks[x].length; z++) {
                Vector2 r = RotationUtil.rotateVector(new Vector2(x, z), rotation);
                int rX = r.getBlockX();
                int rZ = r.getBlockZ();
                for(int y = 0; y < blocks[z].length; y++) {
                    BlockState state = blocks[x][z][y];
                    if(state == null) continue;
                    world.setBlockData(bX + rX, bY + y, bZ + rZ, state);
                }
            }
        }
        return true;
    }
    
    @Override
    public String getID() {
        return id;
    }
}
