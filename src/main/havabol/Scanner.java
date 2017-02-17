package havabol;

import java.io.*;

import static havabol.util.Debug.DEBUG;

public class Scanner
{
    /**
     * Context object to assist in building tokens during scanning phase.
     */
    class ScanContext
    {
        /**
         * Is the current context a String construct?
         *
         * @var boolean
         */
        private boolean isString;

        /**
         * Is the current context a comment?
         *
         * A comment will stretch from a `//` token to the end of line.
         *
         * @var boolean
         */
        private boolean isComment;

        /**
         * Starting index of the current token
         *
         * @var int
         */
        private int beginIdx = -1;

        /**
         * Line number for token
         *
         * @var int
         */
        private int lineIdx = -1;

        /**
         * Index of the cursor inside a line
         *
         * @var int
         */
        private int cursorIdx = -1;

        /**
         * Last character processed by the scanner.
         *
         * @var char
         */
        private char lastCharacter;

        /**
         * Opening string literal character.
         *
         * @var char
         */
        private char strLiteralBegin = 0;

        /**
         * Returns a copy of the scan context with current state.
         *
         * @return ScanContext
         */
        public ScanContext copy() {
            ScanContext ctx = new ScanContext();

            ctx.isString = this.isString;
            ctx.beginIdx = this.beginIdx;
            ctx.lineIdx = this.lineIdx;
            ctx.cursorIdx = this.cursorIdx;
            ctx.lastCharacter = this.lastCharacter;
            ctx.strLiteralBegin = this.strLiteralBegin;

            return ctx;
        }

        /**
         * Override the superclass Object's :toString: to provide scanner
         * context.
         *
         * @return String
         */
        @Override
        public String toString() {
            return String.format(
                "Cursor [b %d ~> c %d] line %d at - strlit? %b",
                this.beginIdx,
                this.cursorIdx,
                this.lineIdx,
                this.isString
            );
        }
    }

    /*
     * CLASS CONSTANTS
     */

    /**
     * List of delimiters that end the construction of a token.
     *
     * @var String
     */
    private final static String delimiters = " \t;:()\'\"=!<>+-*/[]#,^\n";

    /**
     * List of whitespace characters.
     *
     * @var String
     */
    private final static String whitespace = "\t\n ";

    /**
     * End-of-line character
     *
     * @var char
     */
    private final static char endOfLine = '\n';

    /**
     * Valid quote characters.
     *
     * @var String
     */
    private final static String quoteChars = "\'\"";

    /**
     * Escape character.
     *
     * @var char
     */
    private final static char escapeChar = '\\';

    /*
     * INSTANCE VARS
     */

    /**
     * File handle for source file.
     *
     * @var File
     */
    private File sourceFile = null;

    /**
     * Buffered input reader for :sourceFile:
     *
     * @var BufferedReader
     */
    private BufferedReader fileBuf = null;

    /**
     * Symbol table for storing constructed tokens.
     *
     * @var SymbolTable
     */
    protected SymbolTable symbolTable = null;

    /**
     * Exposes the Token instance which is currently being constructed.
     *
     * @var Token
     */
    protected Token currentToken = null;

    /**
     * Scanner's current line.
     *
     * @var String
     */
    protected String currentLine = null;

    /**
     * Stores contextual information about the Token that is currently being
     * constructed.
     *
     * @var ScanContext
     */
    protected ScanContext scanCtx = new ScanContext();

    /**
     * Append a string to a Token's internal string.
     *
     * @param Token t
     * @param String s
     */
    private static void tokenAppend(Token t, String s) {
        t.tokenStr = t.tokenStr + s;
    }

    private static void tokenAppend(Token t, char ch) {
        tokenAppend(t, String.valueOf(ch));
    }

    /**
     * Constructs a Scanner to read code from :sourceFileNm:
     * and store the constructed tokens in :symbolTable:.
     *
     * @param String        sourceFileNm Source file to scan from.
     * @param SymbolTable   symbolTable  Symbol table to store tokens in.
     */
    public Scanner(String sourceFileNm, SymbolTable symbolTable) throws Exception {
        try {
            this.validateAndOpen(sourceFileNm);
        } catch (Exception ex) {
            throw new Exception(
                String.format(
                    "Could not construct Scanner for file %s",
                    sourceFileNm
                ),
                ex
            );
        }
    }

    /**
     * Checks for access to the given file path and opens it, setting
     * :sourceFileName: and :sourceFile: to the newly-opened File handle.
     *
     * SIDE EFFECTS:
     * - Changes {@code this.sourceFile} to be an open file handle to the source file.
     *
     * NOTE: A Scanner can be attached to *one* file. When opening a new file,
     *       you *must* create a new Scanner instance.
     *
     * @param String sourceFileNm Source file to open.
     *
     * @return void
     */
    private void validateAndOpen(String sourceFileNm) throws Exception {
        File tmpf = null;
        try {
            tmpf = new File(sourceFileNm);
        } catch (NullPointerException ex) {
            throw new Exception("Source file name not set in Scanner!");
        }

        if ( tmpf == null ) {
            // Could not open the file.
            throw new Exception(
                String.format(
                    "Could not open handle for file '%s'",
                    sourceFileNm
                )
            );
        }

        if ( !(tmpf.canRead()) ) {
            // No permission to read from the input file?
            throw new Exception(
                String.format(
                    "Could not read from file '%s'",
                    sourceFileNm
                )
            );
        }

        // Was able to create file handle, able to read from the file
        this.sourceFile = tmpf;

        // Now, open the reader buffer.
        FileReader fr;
        try {
            fr = new FileReader(this.sourceFile);
        } catch (FileNotFoundException ex) {
            throw new Exception(
                String.format(
                    "Could not open file buffer for '%s'",
                    sourceFileNm
                ),
                ex
            );
        }

        // Create the buffered reader.
        this.fileBuf = new BufferedReader(fr);

        // Initialize the proper starting values for the scan context
        this.scanCtx.cursorIdx = 0;
        this.scanCtx.beginIdx = 1;
        this.scanCtx.lineIdx = 0;
        this.scanCtx.isString = false;
    }

    /**
     * Returns the open file name or null if none.
     *
     * @return String|null
     */
    public String getSourceFileName() {
        return this.sourceFile.getName();
    }

    /**
     * Returns the current token under the "cursor" or null if scanning has
     * not yet started.
     *
     * @return Token|null
     */
    public Token getCurrentToken() {
        return this.currentToken;
    }

    /**
     * Returns a copy of the current *global* scan context.
     *
     * @return ScanContext
     */
    public ScanContext getCurrentScanContext() {
        return this.scanCtx;
    }

    /**
     * Construct and return the next token or null if fail.
     *
     * Throws an {@code Exception} if the underlying token construction
     * login (ie., {@code constructNextToken}, ...) fails.
     *
     * @return String|null
     */
    public String getNext() throws Exception {
        // Grab the token from constructToken
        Token curToken;
        try {
            curToken = this.constructNextToken();
        } catch (IOException ex) {
            throw new Exception(
                String.format(
                    "Token construction failed: %s %s",
                    this.getSourceFileName(),
                    this.scanCtx
                ),
                ex
            );
        }

        // Instantiate a token for this thing
        this.currentToken = curToken;

        return curToken.tokenStr;
    }

    /**
     * Get the next line from the BufferedReader.
     * Side effects:
     * - Increments {@code this.scanCtx.lineIdx}
     *
     * @return String
     */
    private String getNextLine() throws IOException {
        if ( !this.fileBuf.ready() ) {
            this.scanCtx.lineIdx++;
            return "";
        }

        StringBuilder sb = new StringBuilder();

        char ch;
        while ( this.fileBuf.ready() && (ch = (char) this.fileBuf.read()) != endOfLine ) {
            sb.append(ch);
        }
        sb.append(endOfLine);

        this.scanCtx.lineIdx++;

        return sb.toString();
    }

    /**
     * Performs the actual token processing logic.
     * Also attempts to find errors and ensures the scan context stays up-to-date.
     *
     * Throws an {@code IOException} if the backing {@code BufferedReader}
     * fails to {@code read()} or if {@code ready()} fails.
     *
     * @throws Exception If token construction fails due to an IO-unrelated error.
     * @throws IOException If an IO-related error occurs.
     *
     * @return Token
     */
    private Token constructNextToken() throws Exception {
        StringBuilder sb = new StringBuilder();

        Token t = new Token();

        // If we do not currently have a line, get one.
        if ( this.currentLine == null ) {
            this.currentLine = this.getNextLine();
            System.out.print(
                String.format(
                    "  %d %s",
                    this.scanCtx.lineIdx,
                    this.currentLine
                )
            );
        } else if ( this.currentLine == "" ) {
            // EOF
            t.iSourceLineNr = this.scanCtx.lineIdx;
            t.iColPos = this.scanCtx.beginIdx;
            return t;
        }

        int idx;
        if ( this.scanCtx.cursorIdx != -1 ) {
            idx = this.scanCtx.cursorIdx;
            this.scanCtx.cursorIdx = -1;
        } else {
            idx = 0;
        }
        // Store the token's beginning index.
        this.scanCtx.beginIdx = idx;
        // Loop through characters in the line until we hit a delimiter
        for ( ; idx < this.currentLine.length(); idx++ ) {
            char ch = this.currentLine.charAt(idx);
            // End of line character. Reset currentLine and cursorIdx.
            if ( ch == endOfLine ) {
                DEBUG("Encountered EOL: " + this.scanCtx);
                // Make sure a string literal is not still open at EOL.
                if ( this.scanCtx.isString ) {
                    throw new Exception(
                        String.format(
                            "Unclosed string literal, line %d near col %d",
                            this.scanCtx.lineIdx,
                            this.scanCtx.beginIdx
                        )
                    );
                }

                this.currentLine = null;
                this.scanCtx.beginIdx = -1;
                this.scanCtx.cursorIdx = -1;
                // Short-circuit into the next token.
                return this.constructNextToken();
            }

            // Current character is:
            // - A delimiter,
            // - Not preceded by an escape character '\\',
            // - Not a part of a string literal.
            //
            // This character marks the end of the previous token and the
            // beginning of a delimiter token.
            if ( delimiters.indexOf(ch) != -1 ) {
                DEBUG("Current char: [" + ch + "]");
                DEBUG("Current scan context: " + this.scanCtx);

                // if ( this.scanCtx.lastCharacter != escapeChar && !this.scanCtx.isString )
                // This is an unescaped quote character. What we do depends on the isString flag.
                if ( quoteChars.indexOf(ch) != -1 && this.scanCtx.lastCharacter != escapeChar ) {
                    if ( !this.scanCtx.isString && this.scanCtx.strLiteralBegin == 0 ) {
                        // Classify this token since it requires contextual sensitivity.
                        t.primClassif = Token.OPERAND;
                        t.subClassif = Token.STRING;
                        // We can start a new string literal here.
                        this.scanCtx.beginIdx = idx;
                        this.scanCtx.strLiteralBegin = ch;
                        this.scanCtx.isString = true;
                        continue; // Don't output the string literal character.
                    } else {
                        // We might be closing a string literal here.
                        if ( this.scanCtx.strLiteralBegin == ch ) {
                            this.scanCtx.strLiteralBegin = 0;
                            this.scanCtx.isString = false;
                            continue; // Don't output the string literal character.
                        }
                    }
                }

                // If we've hit a delimiter and we're in a string literal we
                // should probably ignore it.
                if ( this.scanCtx.isString ) {
                    this.scanCtx.lastCharacter = ch;
                    sb.append(ch);
                    continue;
                }

                // If there is a token that follows a longer tokened string,
                // set the cursorIdx back one so it will get picked up properly on
                // the next call.
                if ( sb.length() > 0 ) {
                    // Do *NOT* append to this, pick it up on the next construct
                    // call.
                    this.scanCtx.lastCharacter = ch;
                    this.scanCtx.cursorIdx = idx;
                    break;
                } else if ( sb.length() == 0 && whitespace.indexOf(ch) == -1 ) {
                    // This is a delimiter and the token length is 0.
                    // This will be a "lonely" delimiter token.
                    this.scanCtx.lastCharacter = ch;
                    this.scanCtx.cursorIdx = ++idx;
                    sb.append(ch);
                    break;
                }

                // If this is whitespace & not inside a string literal, ignore.
                if ( !this.scanCtx.isString && whitespace.indexOf(ch) != -1 ) {
                    DEBUG("Encountered whitespace outside string literal");
                    this.scanCtx.lastCharacter = ch;
                    continue;
                }

                this.scanCtx.lastCharacter = ch;
                sb.append(ch);
                // Do not loop again.
                break;
            }

            this.scanCtx.lastCharacter = ch;
            sb.append(ch);
        }

        // Put the built string into the token.
        tokenAppend(t, sb.toString());

        // Set the line and column positions on the token.
        t.iSourceLineNr = this.scanCtx.lineIdx;
        t.iColPos = this.scanCtx.beginIdx;

        // Run the token through the final stage classifier.
        Classifier.classifyToken(t);

        return t;
    }
}
