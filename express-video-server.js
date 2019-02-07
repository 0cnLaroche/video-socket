// uses 'express' package

// MA COMPRÉHENSION DU PROTOCOL
// Le client envois une requette GET http://..../video
app.get('/video', function(req, res) {
  const path = 'assets/sample.mp4'
  const stat = fs.statSync(path)
  const fileSize = stat.size
  const range = req.headers.range // Pour la première requette range sera null

// donc on commence pas ici

// On reprend ici à partir du 2e chunck. Le client connait la taille du fichier
// et va demander un range particulier.
  if (range) {
    // Syntaxe HTTP de l'element Range demandé par le client:
    // <unit>=<range-start>-<range-end>
    // doc: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Range
    const parts = range.replace(/bytes=/, "").split("-")
    const start = parseInt(parts[0], 10)
    const end = parts[1]
      ? parseInt(parts[1], 10)
      : fileSize-1
    // Création du chunk
    const chunksize = (end-start)+1
    const file = fs.createReadStream(path, {start, end})
    // La réponse (status 206)
    const head = {
      'Content-Range': `bytes ${start}-${end}/${fileSize}`,
      'Accept-Ranges': 'bytes',
      'Content-Length': chunksize,
      'Content-Type': 'video/mp4'
      // documentation : https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/206
    }
    // envois de la réponse
    res.writeHead(206, head);
    file.pipe(res);

  } else { // On commence ici car range est NULL pour la première requete
    const head = { // On envois la grosseur du video
      'Content-Length': fileSize,
      'Content-Type': 'video/mp4',
    }
    res.writeHead(200, head)
    fs.createReadStream(path).pipe(res) // On envois le plus gros chunk possible
    // Le client va ensuite demander des chunks spécifiques. On reprend au if
  }
});
