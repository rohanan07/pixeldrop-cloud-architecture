// NEW AWS SDK v3 Syntax (Built-in for Node 18/20)
const { S3Client, GetObjectCommand, PutObjectCommand } = require("@aws-sdk/client-s3");
const sharp = require("sharp");

// Create client
const s3 = new S3Client();

exports.handler = async (event) => {
    try {
        const bucket = event.Records[0].s3.bucket.name;
        const key = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, ' '));

        console.log(`Processing: ${key}`);

        // 1. Sanity Check
        if (!key.startsWith('originals/')) {
            console.log('Skipping: File not in originals folder.');
            return;
        }

        const filename = key.split('/').pop();
        const newKey = `thumbnails/${filename.split('.')[0]}.webp`;

        // 2. Get the image (Updated for SDK v3)
        const getCommand = new GetObjectCommand({ Bucket: bucket, Key: key });
        const { Body } = await s3.send(getCommand);
        
        // Convert stream to buffer (Required for Sharp)
        const imageBuffer = await Body.transformToByteArray();

        // 3. Resize and Compress
        const resizedBuffer = await sharp(imageBuffer)
            .resize({ width: 500 }) // Resize to 500px width
            .webp({ quality: 80 })  // Convert to WebP
            .toBuffer();

        // 4. Upload back to S3 (Updated for SDK v3)
        const putCommand = new PutObjectCommand({
            Bucket: bucket,
            Key: newKey,
            Body: resizedBuffer,
            ContentType: 'image/webp'
        });
        
        await s3.send(putCommand);

        console.log(`Success: Resized to ${newKey}`);
        
    } catch (error) {
        console.error('Error processing image:', error);
        throw error;
    }
};