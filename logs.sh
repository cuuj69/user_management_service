#!/bin/bash

# Liberty Log Management Script
# Usage: ./logs.sh [command]

LOG_DIR="target/liberty/wlp/usr/servers/defaultServer/logs"

case "$1" in
    "tail")
        echo "📋 Tailing messages.log (real-time logs)..."
        tail -f "$LOG_DIR/messages.log"
        ;;
    "messages")
        echo "📋 Showing last 50 lines of messages.log..."
        tail -50 "$LOG_DIR/messages.log"
        ;;
    "errors")
        echo "❌ Showing error logs..."
        grep -i "error\|exception\|severe" "$LOG_DIR/messages.log" | tail -20
        ;;
    "warnings")
        echo "⚠️  Showing warning logs..."
        grep -i "warning" "$LOG_DIR/messages.log" | tail -20
        ;;
    "app")
        echo "🔍 Showing application-specific logs..."
        grep "com.tech11.usermanagement" "$LOG_DIR/messages.log" | tail -20
        ;;
    "clear")
        echo "🧹 Clearing all log files..."
        rm -f "$LOG_DIR"/*.log
        echo "✅ Logs cleared!"
        ;;
    "size")
        echo "📊 Log file sizes:"
        ls -lh "$LOG_DIR"/*.log 2>/dev/null || echo "No log files found"
        ;;
    "help"|"")
        echo "🔧 Liberty Log Management Script"
        echo ""
        echo "Usage: ./logs.sh [command]"
        echo ""
        echo "Commands:"
        echo "  tail     - Follow logs in real-time"
        echo "  messages - Show last 50 log messages"
        echo "  errors   - Show only error messages"
        echo "  warnings - Show only warning messages"
        echo "  app      - Show application-specific logs"
        echo "  clear    - Clear all log files"
        echo "  size     - Show log file sizes"
        echo "  help     - Show this help message"
        echo ""
        echo "Log directory: $LOG_DIR"
        ;;
    *)
        echo "❌ Unknown command: $1"
        echo "Run './logs.sh help' for usage information"
        ;;
esac 