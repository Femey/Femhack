package com.github.lunatrius.schematica.reference;

@SuppressWarnings("HardCodedStringLiteral")
public final class Names {
    public static final class Config {
        public static final class Category {
            public static final String DEBUG = "debug";
            public static final String RENDER = "render";
            public static final String PRINTER = "printer";
            public static final String PRINTER_SWAPSLOTS = "swapslots";
            public static final String GENERAL = "general";
            public static final String SERVER = "server";
        }

        public static final String DUMP_BLOCK_LIST = "dumpBlockList";
        public static final String DUMP_BLOCK_LIST_DESC = "Dump all block states on startup.";
        public static final String SHOW_DEBUG_INFO = "showDebugInfo";
        public static final String SHOW_DEBUG_INFO_DESC = "Display extra information on the debug screen (F3).";

        public static final String ALPHA_ENABLED = "alphaEnabled";
        public static final String ALPHA_ENABLED_DESC = "Enable transparent textures.";
        public static final String ALPHA = "alpha";
        public static final String ALPHA_DESC = "Alpha value used when rendering the schematic (1.0 = opaque, 0.5 = half transparent, 0.0 = transparent).";
        public static final String HIGHLIGHT = "highlight";
        public static final String HIGHLIGHT_DESC = "Highlight invalid placed blocks and to be placed blocks.";
        public static final String HIGHLIGHT_AIR = "highlightAir";
        public static final String HIGHLIGHT_AIR_DESC = "Highlight blocks that should be air.";
        public static final String BLOCK_DELTA = "blockDelta";
        public static final String BLOCK_DELTA_DESC = "Delta value used for highlighting (if you experience z-fighting increase this).";
        public static final String RENDER_DISTANCE = "renderDistance";
        public static final String RENDER_DISTANCE_DESC = "Schematic render distance.";

        public static final String PLACE_DELAY = "placeDelay";
        public static final String PLACE_DELAY_DESC = "Delay between placement attempts (in ticks).";
        public static final String TIMEOUT = "timeout";
        public static final String TIMEOUT_DESC = "Timeout before re-trying failed blocks.";
        public static final String PLACE_DISTANCE = "placeDistance";
        public static final String PLACE_DISTANCE_DESC = "Maximum placement distance.";
        public static final String PLACE_INSTANTLY = "placeInstantly";
        public static final String PLACE_INSTANTLY_DESC = "Place all blocks that can be placed in one tick.";
        public static final String DESTROY_BLOCKS = "destroyBlocks";
        public static final String DESTROY_BLOCKS_DESC = "The printer will destroy blocks (creative mode only).";
        public static final String DESTROY_INSTANTLY = "destroyInstantly";
        public static final String DESTROY_INSTANTLY_DESC = "Destroy all blocks that can be destroyed in one tick.";
        public static final String PLACE_ADJACENT = "placeAdjacent";
        public static final String PLACE_ADJACENT_DESC = "Place blocks only if there is an adjacent block next to them.";
        public static final String SWAP_SLOT = "swapSlot";
        public static final String SWAP_SLOT_DESC = "Allow the printer to use this hotbar slot.";

        public static final String SCHEMATIC_DIRECTORY = "schematicDirectory";
        public static final String SCHEMATIC_DIRECTORY_DESC = "Schematic directory.";
        public static final String EXTRA_AIR_BLOCKS = "extraAirBlocks";
        public static final String EXTRA_AIR_BLOCKS_DESC = "Extra blocks to consider as air for the schematic renderer.";
        public static final String SORT_TYPE = "sortType";
        public static final String SORT_TYPE_DESC = "Default sort type for the material list.";

        public static final String PRINTER_ENABLED = "printerEnabled";
        public static final String PRINTER_ENABLED_DESC = "Allow players to use the printer.";
        public static final String SAVE_ENABLED = "saveEnabled";
        public static final String SAVE_ENABLED_DESC = "Allow players to save schematics.";
        public static final String LOAD_ENABLED = "loadEnabled";
        public static final String LOAD_ENABLED_DESC = "Allow players to load schematics.";

        public static final String PLAYER_QUOTA_KILOBYTES = "playerQuotaKilobytes";
        public static final String PLAYER_QUOTA_KILOBYTES_DESC = "Amount of storage provided per-player for schematics on the server.";

        public static final String LANG_PREFIX = Reference.MODID + ".config";
    }

    public static final class Command {
        public static final class Save {
            public static final class Message {
                public static final String USAGE = "Usage";
                public static final String PLAYERS_ONLY = "Players Only";
                public static final String SAVE_STARTED = "Save Started";
                public static final String SAVE_SUCCESSFUL = "Save Succeeded";
                public static final String SAVE_FAILED = "Save Failed";
                public static final String QUOTA_EXCEEDED = "Quota Exceeded";
                public static final String PLAYER_SCHEMATIC_DIR_UNAVAILABLE = "Player Schematic Dir Unavailable";
                public static final String UNKNOWN_FORMAT = "Unknown Format";
            }

            public static final String NAME = "schematicaSave";
        }

        public static final class List {
            public static final class Message {
                public static final String USAGE = "Usage";
                public static final String LIST_NOT_AVAILABLE = "List Not Available";
                public static final String REMOVE = "Remove";
                public static final String DOWNLOAD = "Download";
                public static final String PAGE_HEADER = "Header";
                public static final String NO_SUCH_PAGE = "No Such Page";
                public static final String NO_SCHEMATICS = "No Schematics";
            }

            public static final String NAME = "schematicaList";
        }

        public static final class Remove {
            public static final class Message {
                public static final String USAGE = "Usage";
                public static final String PLAYERS_ONLY = "Players Only";
                public static final String SCHEMATIC_REMOVED = "Schematic Removed";
                public static final String SCHEMATIC_NOT_FOUND = "Schematic Not Found";
                public static final String ARE_YOU_SURE_START = "Are You Sure";
                public static final String YES = "Yes";
            }

            public static final String NAME = "schematicaRemove";
        }

        public static final class Download {
            public static final class Message {
                public static final String USAGE = "Usage";
                public static final String PLAYERS_ONLY = "Players Only";
                public static final String DOWNLOAD_STARTED = "Download Started";
                public static final String DOWNLOAD_SUCCEEDED = "Download Succeeded";
                public static final String DOWNLOAD_FAILED = "Download Fail";
            }

            public static final String NAME = "schematicaDownload";
        }

        public static final class Replace {
            public static final class Message {
                public static final String USAGE = "Usage";
                public static final String NO_SCHEMATIC = "No Schematic";
                public static final String SUCCESS = "Success";
            }

            public static final String NAME = "schematicaReplace";
        }
    }

    public static final class Messages {
        public static final String TOGGLE_PRINTER = "Toggle Printer";

        public static final String INVALID_BLOCK = "Invalid Block";
        public static final String INVALID_PROPERTY = "Invalid Property";
        public static final String INVALID_PROPERTY_FOR_BLOCK = "Invalid Property For Block";
    }

    public static final class Gui {
        public static final class Load {
            public static final String TITLE = "Title";
            public static final String FOLDER_INFO = "Folder Info";
            public static final String OPEN_FOLDER = "Open Folder";
            public static final String NO_SCHEMATIC = "No Schematic";
        }

        public static final class Save {
            public static final String POINT_RED = "Point Red";
            public static final String POINT_BLUE = "Point Blue";
            public static final String SAVE = "Save";
            public static final String SAVE_SELECTION = "Save Selection";
            public static final String FORMAT = "Format";
        }

        public static final class Control {
            public static final String MOVE_SCHEMATIC = "Move Schematic";
            public static final String MATERIALS = "Materials";
            public static final String PRINTER = "Printer";
            public static final String OPERATIONS = "Operations";

            public static final String UNLOAD = "Unload";
            public static final String MODE_ALL = "All";
            public static final String MODE_LAYERS = "Layers";
            public static final String MODE_BELOW = "Below";
            public static final String HIDE = "Hide";
            public static final String SHOW = "Show";
            public static final String MOVE_HERE = "Move Here";
            public static final String FLIP = "Flip";
            public static final String ROTATE = "Rotate";
            public static final String TRANSFORM_PREFIX = "";

            public static final String MATERIAL_NAME = "Material Name";
            public static final String MATERIAL_AMOUNT = "Material Amount";
            public static final String MATERIAL_AVAILABLE = "Material Available";
            public static final String MATERIAL_MISSING = "Material Missing";

            public static final String SORT_PREFIX = "Material";
            public static final String DUMP = "Material Dump";
        }

        public static final String X = "X";
        public static final String Y = "Y";
        public static final String Z = "Z";
        public static final String ON = "On";
        public static final String OFF = "Off";
        public static final String DONE = "Done";
    }

    public static final class ModId {
        public static final String MINECRAFT = "minecraft";
    }

    public static final class Keys {
        public static final String CATEGORY = "Category";
        public static final String LOAD = "Load";
        public static final String SAVE = "Save";
        public static final String CONTROL = "Control";
        public static final String LAYER_INC = "Layer Inc";
        public static final String LAYER_DEC = "Layer Dec";
        public static final String LAYER_TOGGLE = "Layer Toggle";
        public static final String RENDER_TOGGLE = "Render Toggle";
        public static final String PRINTER_TOGGLE = "Printer Toggle";
        public static final String MOVE_HERE = "Move Here";
        public static final String PICK_BLOCK = "Pick Block";
    }

    public static final class NBT {
        public static final String ROOT = "Schematic";

        public static final String MATERIALS = "Materials";
        public static final String FORMAT_CLASSIC = "Classic";
        public static final String FORMAT_ALPHA = "Alpha";
        public static final String FORMAT_STRUCTURE = "Structure";

        public static final String ICON = "Icon";
        public static final String BLOCKS = "Blocks";
        public static final String DATA = "Data";
        public static final String ADD_BLOCKS = "AddBlocks";
        public static final String ADD_BLOCKS_SCHEMATICA = "Add";
        public static final String WIDTH = "Width";
        public static final String LENGTH = "Length";
        public static final String HEIGHT = "Height";
        public static final String MAPPING_SCHEMATICA = "SchematicaMapping";
        public static final String TILE_ENTITIES = "TileEntities";
        public static final String ENTITIES = "Entities";
        public static final String EXTENDED_METADATA = "ExtendedMetadata";
    }

    public static final class Formats {
        public static final String CLASSIC = "Classic";
        public static final String ALPHA = "Alpha";
        public static final String STRUCTURE = "Structure";
        public static final String INVALID = "Invalid";
    }

    public static final class Extensions {
        public static final String SCHEMATIC = ".schematic";
        public static final String STRUCTURE = ".nbt";
    }
}
