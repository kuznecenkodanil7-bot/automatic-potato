package ru.wqkcpf.moderationhelper.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import ru.wqkcpf.moderationhelper.ModerationHelperClient;
import ru.wqkcpf.moderationhelper.punishment.PunishmentType;

public abstract class BaseModScreen extends Screen {
    protected BaseModScreen(String title) {
        super(Text.literal(title));
    }

    protected ButtonWidget button(int x, int y, int w, int h, String label, ButtonWidget.PressAction action) {
        return ButtonWidget.builder(Text.literal(label), action).dimensions(x, y, w, h).build();
    }


    protected void renderDarkOverlay(DrawContext context) {
        context.fill(0, 0, width, height, 0x99000000);
    }

    protected void renderPunishmentBadge(DrawContext context, PunishmentType selected, int x, int y) {
        renderPanel(context, x, y, 230, 34);
        context.drawTextWithShadow(textRenderer, Text.literal("Выбрано: " + selected.label), x + 10, y + 8, 0xFFFFDD55);
        context.fill(x + 10, y + 25, x + 220, y + 27, 0xFFFFDD55);
    }

    protected void renderCategoryHint(DrawContext context, int x, int y) {
        context.drawTextWithShadow(textRenderer, Text.literal("Выбери категорию наказания. Следующие окна подсветят выбранный тип."), x, y, 0xFFBBBBBB);
    }

    protected void renderPanel(DrawContext context, int x, int y, int w, int h) {
        context.fill(x, y, x + w, y + h, 0xCC10131A);
        context.fill(x, y, x + w, y + 1, 0xFF2E8BFF);
        context.fill(x, y + h - 1, x + w, y + h, 0x662E8BFF);
    }

    protected void renderTitle(DrawContext context, String text, int x, int y) {
        context.drawTextWithShadow(textRenderer, Text.literal(text), x, y, 0xFFFFFFFF);
    }

    protected void renderStats(DrawContext context, int x, int y) {
        renderPanel(context, x, y, 145, 92);
        context.drawTextWithShadow(textRenderer, Text.literal("Статистика сессии"), x + 10, y + 10, 0xFF55C7FF);
        context.drawTextWithShadow(textRenderer, Text.literal("warn: " + ModerationHelperClient.STATS.get(PunishmentType.WARN)), x + 10, y + 28, 0xFFEAEAEA);
        context.drawTextWithShadow(textRenderer, Text.literal("mute: " + ModerationHelperClient.STATS.get(PunishmentType.MUTE)), x + 10, y + 43, 0xFFEAEAEA);
        context.drawTextWithShadow(textRenderer, Text.literal("ban: " + ModerationHelperClient.STATS.get(PunishmentType.BAN)), x + 10, y + 58, 0xFFEAEAEA);
        context.drawTextWithShadow(textRenderer, Text.literal("ipban: " + ModerationHelperClient.STATS.get(PunishmentType.IPBAN)), x + 10, y + 73, 0xFFEAEAEA);
    }

    protected void renderRecent(DrawContext context, int x, int y, int w, boolean hint) {
        int h = Math.max(76, 24 + ModerationHelperClient.RECENT_PLAYERS.getPlayers().size() * 14);
        renderPanel(context, x, y, w, h);
        context.drawTextWithShadow(textRenderer, Text.literal("Недавние игроки"), x + 10, y + 10, 0xFF55C7FF);
        int yy = y + 28;
        for (String p : ModerationHelperClient.RECENT_PLAYERS.getPlayers()) {
            context.drawTextWithShadow(textRenderer, Text.literal(p), x + 10, yy, 0xFFFFFFFF);
            yy += 14;
        }
        if (hint && ModerationHelperClient.RECENT_PLAYERS.getPlayers().isEmpty()) {
            context.drawTextWithShadow(textRenderer, Text.literal("Пока пусто"), x + 10, yy, 0xFFAAAAAA);
        }
    }

    protected boolean handleRecentClick(double mouseX, double mouseY, int x, int y, int w) {
        int yy = y + 28;
        for (String p : ModerationHelperClient.RECENT_PLAYERS.getPlayers()) {
            if (mouseX >= x + 8 && mouseX <= x + w - 8 && mouseY >= yy - 2 && mouseY <= yy + 11) {
                if (client != null) {
                    client.keyboard.setClipboard(p);
                    client.setScreen(new PunishmentScreen(p, null));
                }
                return true;
            }
            yy += 14;
        }
        return false;
    }
}
