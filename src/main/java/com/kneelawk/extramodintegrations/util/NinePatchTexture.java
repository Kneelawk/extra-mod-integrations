package com.kneelawk.extramodintegrations.util;

import dev.emi.emi.EmiPort;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class NinePatchTexture {
    private final Identifier textureId;
    private final int textureWidth, textureHeight;
    private final int leftWidth, rightWidth, topHeight, bottomHeight;
    private final boolean tiling;
    private final int tileWidth, tileHeight;
    private final int leftRight, topBottom;
    private final float pieceU1, pieceV1, pieceU2, pieceV2;
    private final float leftRightU, rightLeftU, topBottomV, bottomTopV;

    public NinePatchTexture(Identifier textureId, int u, int v, int width, int height, int leftWidth, int rightWidth,
                            int topHeight, int bottomHeight, boolean tiling) {
        this(textureId, 256, 256, u, v, width, height, leftWidth, rightWidth, topHeight, bottomHeight, tiling);
    }

    public NinePatchTexture(Identifier textureId, int textureWidth, int textureHeight, int u, int v, int width,
                            int height, int leftWidth, int rightWidth, int topHeight, int bottomHeight,
                            boolean tiling) {
        this.textureId = textureId;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.leftWidth = leftWidth;
        this.rightWidth = rightWidth;
        this.topHeight = topHeight;
        this.bottomHeight = bottomHeight;
        this.tiling = tiling;
        int endX = u + width;
        int endY = v + height;
        tileWidth = width - leftWidth - rightWidth;
        tileHeight = height - topHeight - bottomHeight;

        if (tileWidth < 1) {
            throw new IllegalArgumentException("leftWidth + rightWidth must be less than pieceWidth");
        }
        if (tileHeight < 1) {
            throw new IllegalArgumentException("topHeight + bottomHeight must be less than pieceHeight");
        }

        leftRight = u + leftWidth;
        topBottom = v + topHeight;

        pieceU1 = (float) u / (float) textureWidth;
        pieceV1 = (float) v / (float) textureHeight;
        pieceU2 = (float) endX / (float) textureWidth;
        pieceV2 = (float) endY / (float) textureHeight;
        leftRightU = (float) (u + leftWidth) / (float) textureWidth;
        rightLeftU = (float) (endX - rightWidth) / (float) textureWidth;
        topBottomV = (float) (v + topHeight) / (float) textureHeight;
        bottomTopV = (float) (endY - bottomHeight) / (float) textureHeight;
    }

    public void render(MatrixStack stack, int x, int y, int w, int h) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, textureId);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        render(bufferBuilder, stack.peek().getPositionMatrix(), 0, x, y, w, h);
        EmiPort.draw(bufferBuilder);
    }

    private void render(VertexConsumer consumer, Matrix4f mat, int z, int x, int y, int w, int h) {
        int centerWidth = w - leftWidth - rightWidth;
        int centerHeight = h - topHeight - bottomHeight;
        int rightLeft = x + w - rightWidth;
        int bottomTop = y + h - bottomHeight;

        // draw 4 corners
        rect(consumer, mat, z, x, y, leftWidth, topHeight, pieceU1, pieceV1, leftRightU, topBottomV);
        rect(consumer, mat, z, rightLeft, y, rightWidth, topHeight, rightLeftU, pieceV1, pieceU2, topBottomV);
        rect(consumer, mat, z, x, bottomTop, leftWidth, bottomHeight, pieceU1, bottomTopV, leftRightU, pieceV2);
        rect(consumer, mat, z, rightLeft, bottomTop, rightWidth, bottomHeight, rightLeftU, bottomTopV, pieceU2,
            pieceV2);

        if (tiling) {
            int tilesX = (centerWidth + tileWidth - 1) / tileWidth;
            int tilesY = (centerHeight + tileHeight - 1) / tileHeight;

            // draw top and bottom edge tiles
            for (int tileXIndex = 0; tileXIndex < tilesX; tileXIndex++) {
                int localTileX = tileXIndex * tileWidth;
                int curTileWidth = Math.min(tileWidth, centerWidth - localTileX);
                float curTileU2 = (float) (leftRight + curTileWidth) / (float) textureWidth;
                rect(consumer, mat, z, leftWidth + localTileX + x, y, curTileWidth, topHeight, leftRightU, pieceV1,
                    curTileU2, topBottomV);
                rect(consumer, mat, z, leftWidth + localTileX + x, bottomTop, curTileWidth, bottomHeight, leftRightU,
                    bottomTopV, curTileU2, pieceV2);
            }

            for (int tileYIndex = 0; tileYIndex < tilesY; tileYIndex++) {
                int localTileY = tileYIndex * tileHeight;
                int curTileHeight = Math.min(tileHeight, centerHeight - localTileY);
                float curTileV2 = (float) (topBottom + curTileHeight) / (float) textureHeight;

                // draw left and right edge tiles
                rect(consumer, mat, z, x, topHeight + localTileY + y, leftWidth, curTileHeight, pieceU1, topBottomV,
                    leftRightU, curTileV2);
                rect(consumer, mat, z, rightLeft, topHeight + localTileY + y, rightWidth, curTileHeight, rightLeftU,
                    topBottomV, pieceU2, curTileV2);

                // draw center tiles
                for (int tileXIndex = 0; tileXIndex < tilesX; tileXIndex++) {
                    int localTileX = tileXIndex * tileWidth;
                    int curTileWidth = Math.min(tileWidth, centerWidth - localTileX);
                    float curTileU2 = (float) (leftRight + curTileWidth) / (float) textureWidth;
                    rect(consumer, mat, z, leftWidth + localTileX + x, topHeight + localTileY + y, curTileWidth,
                        curTileHeight, leftRightU, topBottomV, curTileU2, curTileV2);
                }
            }
        } else {
            // draw top and bottom edges
            if (centerWidth > 0) {
                rect(consumer, mat, z, leftWidth + x, y, centerWidth, topHeight, leftRightU, pieceV1, rightLeftU,
                    topBottomV);
                rect(consumer, mat, z, leftWidth + x, bottomTop, centerWidth, bottomHeight, leftRightU, bottomTopV,
                    rightLeftU, pieceV2);
            }

            if (centerHeight > 0) {
                // draw left and right edges
                rect(consumer, mat, z, x, topHeight + y, leftWidth, centerHeight, pieceU1, topBottomV, leftRightU,
                    bottomTopV);
                rect(consumer, mat, z, rightLeft, topHeight + y, rightWidth, centerHeight, rightLeftU, topBottomV,
                    pieceU2, bottomTopV);

                // draw center
                if (centerWidth > 0) {
                    rect(consumer, mat, z, leftWidth + x, topHeight + y, centerWidth, centerHeight, leftRightU,
                        topBottomV, rightLeftU, bottomTopV);
                }
            }
        }
    }

    private static void rect(VertexConsumer consumer, Matrix4f mat, int z, int x0, int y0, int w, int h, float u0,
                             float v0, float u1, float v1) {
        int x1 = x0 + w;
        int y1 = y0 + h;
        consumer.vertex(mat, x0, y1, z).texture(u0, v1).next();
        consumer.vertex(mat, x1, y1, z).texture(u1, v1).next();
        consumer.vertex(mat, x1, y0, z).texture(u1, v0).next();
        consumer.vertex(mat, x0, y0, z).texture(u0, v0).next();
    }
}
