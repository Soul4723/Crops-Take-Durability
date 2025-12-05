# CropsTakeDurability

CropsTakeDurability is a simple Spigot/Paper plugin that makes tools lose durability when harvesting certain crops. The list of affected crops is configurable.

## Requirements

- Java 16 or newer
- Spigot or Paper 1.17+

## Installation

1. Build the plugin or download the jar.
2. Place the jar file in the `plugins` folder of your Spigot or Paper server.
3. Start or restart your server.
4. A default `config.yml` will be created in the plugin's data folder on first run.

## Configuration

The main setting is a list of block types that should cause hoes to lose durability when broken.

`config.yml` example:

```yaml
crop-materials:
  - "WHEAT"
  - "CARROTS"
  - "POTATOES"
  - "BEETROOTS"
  - "NETHER_WART"
  - "SWEET_BERRY_BUSH"
  - "CAVE_VINES"
  - "CAVE_VINES_PLANT"
  - "BROWN_MUSHROOM"
  - "RED_MUSHROOM"
  - "COCOA"
  - "TORCHFLOWER"
  - "PITCHER_PLANT"
  - "CRIMSON_FUNGUS"
  - "WARPED_FUNGUS"
```

Notes:

- Values must match Bukkit `Material` names. They are treated case-insensitively.
- Any invalid material names will be logged as warnings and ignored.
- If no valid materials are configured, the plugin will log a warning and will not affect any blocks.

## Behavior

- When a player breaks a block listed in `crop-materials`, the plugin checks the player's main hand and off hand.
- If the item is a hoe, it will lose 1 durability when harvesting a configured crop.
- The Unbreaking enchantment is respected. The chance to avoid damage is `1 / (level + 1)`.
- If a hoe reaches its maximum damage, it breaks and is removed from the player's hand, and the item break sound is played.

## Commands and permissions

Commands:

- `/cropsdurability reload` - Reloads the plugin configuration.

Permissions:

- `cropstakedurability.reload` - Allows reloading the configuration. Default: op.
- `cropstakedurability.bypass` - Prevents hoes from taking durability when harvesting configured crops. Default: false.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
